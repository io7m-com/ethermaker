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

package com.io7m.ethermaker.tests;

import com.io7m.ethermaker.core.MACAddress;
import com.io7m.ethermaker.core.MACAddresses;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MACAddressesTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(MACAddressesTest.class);

  @Test
  public void equalsCorrect()
  {
    EqualsVerifier.forClass(MACAddress.class)
      .verify();
  }

  @Test
  public void isLocal()
  {
    final var addr = MACAddresses.parse("02:00:00:00:00:00");
    assertEquals(0x02, addr.octet0());
    assertEquals(0x00, addr.octet1());
    assertEquals(0x00, addr.octet2());
    assertEquals(0x00, addr.octet3());
    assertEquals(0x00, addr.octet4());
    assertEquals(0x00, addr.octet5());
    assertTrue(addr.isLocallyAdministered());
    assertFalse(addr.isMulticast());
    assertFalse(addr.isBroadcast());
    assertEquals("02:00:00:00:00:00", addr.toString());
    assertEquals("020000", addr.organization());
  }

  @Test
  public void isMulticast()
  {
    final var addr = MACAddresses.parse("01:00:00:00:00:00");
    assertEquals(0x01, addr.octet0());
    assertEquals(0x00, addr.octet1());
    assertEquals(0x00, addr.octet2());
    assertEquals(0x00, addr.octet3());
    assertEquals(0x00, addr.octet4());
    assertEquals(0x00, addr.octet5());
    assertFalse(addr.isLocallyAdministered());
    assertTrue(addr.isMulticast());
    assertFalse(addr.isBroadcast());
    assertEquals("01:00:00:00:00:00", addr.toString());
    assertEquals("010000", addr.organization());
  }

  @Test
  public void isBroadcast()
  {
    final var addr = MACAddresses.parse("ff:ff:ff:ff:ff:ff");
    assertEquals(0xff, addr.octet0());
    assertEquals(0xff, addr.octet1());
    assertEquals(0xff, addr.octet2());
    assertEquals(0xff, addr.octet3());
    assertEquals(0xff, addr.octet4());
    assertEquals(0xff, addr.octet5());
    assertTrue(addr.isLocallyAdministered());
    assertTrue(addr.isMulticast());
    assertTrue(addr.isBroadcast());
    assertEquals("ff:ff:ff:ff:ff:ff", addr.toString());
    assertEquals("ffffff", addr.organization());
  }

  @Test
  public void parseOK()
  {
    final var addr = MACAddresses.parse("00:10:20:30:40:50");
    assertEquals(0x00, addr.octet0());
    assertEquals(0x10, addr.octet1());
    assertEquals(0x20, addr.octet2());
    assertEquals(0x30, addr.octet3());
    assertEquals(0x40, addr.octet4());
    assertEquals(0x50, addr.octet5());
    assertEquals("00:10:20:30:40:50", addr.toString());
    assertEquals("001020", addr.organization());
  }

  @Test
  public void parseOKSpaced()
  {
    final var addr = MACAddresses.parse("       00:10:20:30:40:50   ");
    assertEquals(0x00, addr.octet0());
    assertEquals(0x10, addr.octet1());
    assertEquals(0x20, addr.octet2());
    assertEquals(0x30, addr.octet3());
    assertEquals(0x40, addr.octet4());
    assertEquals(0x50, addr.octet5());
    assertEquals("00:10:20:30:40:50", addr.toString());
    assertEquals("001020", addr.organization());
  }

  @Test
  public void parseOrgOK()
  {
    final var addr = MACAddresses.parseOrganization("F497C2");
    assertEquals(0xf4, addr.octet0());
    assertEquals(0x97, addr.octet1());
    assertEquals(0xc2, addr.octet2());
    assertEquals(0x0, addr.octet3());
    assertEquals(0x0, addr.octet4());
    assertEquals(0x0, addr.octet5());
    assertEquals("f4:97:c2:00:00:00", addr.toString());
    assertEquals("f497c2", addr.organization());
  }

  @Test
  public void parseOrgFailed0()
  {
    final var x = assertThrows(IllegalArgumentException.class, () -> {
      MACAddresses.parseOrganization("x");
    });
    LOG.error("error: ", x);
  }

  @Test
  public void parseFailed0()
  {
    final var x = assertThrows(IllegalArgumentException.class, () -> {
      MACAddresses.parse("x");
    });
    LOG.error("error: ", x);
  }

  @Test
  public void parseOrgFailed1()
  {
    final var x = assertThrows(IllegalArgumentException.class, () -> {
      MACAddresses.parseOrganization("fffffff");
    });
    LOG.error("error: ", x);
  }

  @Test
  public void parseFailed1()
  {
    final var x = assertThrows(IllegalArgumentException.class, () -> {
      MACAddresses.parse("fff:ff:ff:ff:ff:ff");
    });
    LOG.error("error: ", x);
  }

  @Test
  public void generation()
    throws Exception
  {
    final SecureRandom rng = SecureRandom.getInstanceStrong();
    for (int index = 0; index < 1000; ++index) {
      final var address = MACAddresses.generate(Optional.empty(), rng);
      assertTrue(MACAddresses.asLocallyAdministered(address)
                   .isLocallyAdministered());
      assertTrue(MACAddresses.asMulticast(address)
                   .isMulticast());
      assertFalse(MACAddresses.asUnicast(address)
                    .isMulticast());
      assertFalse(MACAddresses.asOUIEnforced(address)
                    .isLocallyAdministered());
    }
  }

  @Test
  public void generationCopied()
    throws Exception
  {
    final SecureRandom rng = SecureRandom.getInstanceStrong();
    for (int index = 0; index < 1000; ++index) {
      final var org = MACAddresses.generate(Optional.empty(), rng);
      final var address = MACAddresses.generate(Optional.of(org), rng);
      assertEquals(org.organization(), address.organization());
      assertEquals(
        Boolean.valueOf(org.isBroadcast()),
        Boolean.valueOf(address.isBroadcast()));
      assertEquals(
        Boolean.valueOf(org.isLocallyAdministered()),
        Boolean.valueOf(address.isLocallyAdministered()));
      assertEquals(
        Boolean.valueOf(org.isMulticast()),
        Boolean.valueOf(address.isMulticast()));
    }
  }
}
