/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.ethermaker.core;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Functions over MAC addresses.
 */

public final class MACAddresses
{
  private static final Pattern VALID_MAC_ADDRESSES =
    Pattern.compile(
      "([a-f0-9]{2}):([a-f0-9]{2}):([a-f0-9]{2}):([a-f0-9]{2}):([a-f0-9]{2}):([a-f0-9]{2})",
      Pattern.CASE_INSENSITIVE
    );

  private static final Pattern VALID_ORGANIZATION =
    Pattern.compile(
      "([a-f0-9]{6})",
      Pattern.CASE_INSENSITIVE
    );

  private static final ResourceBundle RESOURCES =
    ResourceBundle.getBundle("com.io7m.ethermaker.core.Messages");

  private MACAddresses()
  {

  }

  /**
   * @param address The address
   *
   * @return The given address as a unicast address
   */

  public static MACAddress asUnicast(
    final MACAddress address)
  {
    return MACAddress.builder()
      .from(address)
      .setOctet0(address.octet0() & ~0b0000_0001)
      .build();
  }

  /**
   * @param address The address
   *
   * @return The given address as a multicast address
   */

  public static MACAddress asMulticast(
    final MACAddress address)
  {
    return MACAddress.builder()
      .from(address)
      .setOctet0(address.octet0() | 0b0000_0001)
      .build();
  }

  /**
   * @param address The address
   *
   * @return The given address as an OUI enforced address
   */

  public static MACAddress asOUIEnforced(
    final MACAddress address)
  {
    return MACAddress.builder()
      .from(address)
      .setOctet0(address.octet0() & ~0b0000_0010)
      .build();
  }

  /**
   * @param address The address
   *
   * @return The given address as a locally administered address
   */

  public static MACAddress asLocallyAdministered(
    final MACAddress address)
  {
    return MACAddress.builder()
      .from(address)
      .setOctet0(address.octet0() | 0b0000_0010)
      .build();
  }

  /**
   * Generate a random MAC address, copying the OUI octets from the given
   * organization, if one is provided.
   *
   * @param organization The organization base
   * @param random       A random number generator
   *
   * @return A generated address
   */

  public static MACAddress generate(
    final Optional<MACAddress> organization,
    final Random random)
  {
    Objects.requireNonNull(organization, "organization");
    Objects.requireNonNull(random, "random");

    var o0 = random.nextInt(256);
    var o1 = random.nextInt(256);
    var o2 = random.nextInt(256);
    final var o3 = random.nextInt(256);
    final var o4 = random.nextInt(256);
    final var o5 = random.nextInt(256);

    /*
     * If an organization was given, overwrite the OUI octets.
     */

    if (organization.isPresent()) {
      final var org = organization.get();
      o0 = org.octet0();
      o1 = org.octet1();
      o2 = org.octet2();
    }

    return MACAddress.builder()
      .setOctet0(o0)
      .setOctet1(o1)
      .setOctet2(o2)
      .setOctet3(o3)
      .setOctet4(o4)
      .setOctet5(o5)
      .build();
  }

  /**
   * Parse a MAC address.
   *
   * @param text The input text
   *
   * @return A parsed MAC address
   */

  public static MACAddress parse(
    final String text)
  {
    Objects.requireNonNull(text, "text");

    final var matcher = VALID_MAC_ADDRESSES.matcher(text.trim());
    if (matcher.matches()) {
      final var o0 = matcher.group(1);
      final var o1 = matcher.group(2);
      final var o2 = matcher.group(3);
      final var o3 = matcher.group(4);
      final var o4 = matcher.group(5);
      final var o5 = matcher.group(6);
      return MACAddress.builder()
        .setOctet0(Integer.parseInt(o0, 16))
        .setOctet1(Integer.parseInt(o1, 16))
        .setOctet2(Integer.parseInt(o2, 16))
        .setOctet3(Integer.parseInt(o3, 16))
        .setOctet4(Integer.parseInt(o4, 16))
        .setOctet5(Integer.parseInt(o5, 16))
        .build();
    }

    throw new IllegalArgumentException(
      MessageFormat.format(
        RESOURCES.getString("errorMacAddress"),
        VALID_MAC_ADDRESSES,
        text
      )
    );
  }

  /**
   * Parse an organization value, such as "C419D1".
   *
   * @param text The input text
   *
   * @return A zeroed address using the parsed organization
   */

  public static MACAddress parseOrganization(
    final String text)
  {
    Objects.requireNonNull(text, "text");

    final var matcher = VALID_ORGANIZATION.matcher(text.trim());
    if (matcher.matches()) {
      final var base = Integer.parseInt(matcher.group(1), 16);
      final var o0 = (base & 0x00ff0000) >>> 16;
      final var o1 = (base & 0x0000ff00) >>> 8;
      final var o2 = (base & 0x000000ff);
      return MACAddress.builder()
        .setOctet0(o0)
        .setOctet1(o1)
        .setOctet2(o2)
        .setOctet3(0)
        .setOctet4(0)
        .setOctet5(0)
        .build();
    }

    throw new IllegalArgumentException(
      MessageFormat.format(
        RESOURCES.getString("errorOrg"),
        VALID_ORGANIZATION,
        text
      )
    );
  }
}
