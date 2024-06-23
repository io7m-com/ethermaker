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

package com.io7m.ethermaker.cmdline.internal;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.io7m.claypot.core.CLPAbstractCommand;
import com.io7m.claypot.core.CLPCommandContextType;
import com.io7m.ethermaker.core.MACAddress;
import com.io7m.ethermaker.core.MACAddresses;
import com.io7m.jranges.RangeCheck;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;

import static com.io7m.claypot.core.CLPCommandType.Status.SUCCESS;

/**
 * The "generate" command.
 */

@Parameters(commandDescription = "Generate MAC addresses")
public final class EMCommandGenerate extends CLPAbstractCommand
{
  @Parameter(
    required = false,
    names = "--organization",
    description = "The OUI, such as 'C42996'")
  private String organization;

  @Parameter(
    required = false,
    names = "--local",
    arity = 1,
    description = "Make the address(es) locally administered")
  private boolean forceLocal;

  @Parameter(
    required = false,
    names = "--unicast",
    arity = 1,
    description = "Make the address(es) a unicast address")
  private boolean forceUnicast = true;

  @Parameter(
    required = false,
    names = "--multicast",
    arity = 1,
    description = "Make the address(es) a multicast address")
  private boolean forceMulticast;

  @Parameter(
    required = false,
    names = "--count",
    description = "The number of addresses to generate")
  private int count = 1;

  /**
   * Construct a command.
   *
   * @param inContext The command context
   */

  public EMCommandGenerate(
    final CLPCommandContextType inContext)
  {
    super(inContext);
  }

  @Override
  public String extendedHelp()
  {
    return EMMessages.create()
      .format("helpGenerate");
  }

  @Override
  protected Status executeActual()
    throws Exception
  {
    RangeCheck.checkGreaterEqualInteger(
      this.count,
      "Count",
      0,
      "Minimum count"
    );

    final Optional<MACAddress> orgBase;
    if (this.organization != null) {
      orgBase = Optional.of(MACAddresses.parseOrganization(this.organization));
    } else {
      orgBase = Optional.empty();
    }

    final var rng = SecureRandom.getInstanceStrong();
    final var numbers = new HashSet<MACAddress>(this.count);
    while (numbers.size() != this.count) {
      var address = MACAddresses.generate(orgBase, rng);
      if (address.isBroadcast()) {
        continue;
      }
      if (this.forceMulticast) {
        address = MACAddresses.asMulticast(address);
      }
      if (this.forceUnicast) {
        address = MACAddresses.asUnicast(address);
      }
      if (this.forceLocal) {
        address = MACAddresses.asLocallyAdministered(address);
      }
      numbers.add(address);
    }

    for (final var number : numbers) {
      System.out.println(number);
    }
    return SUCCESS;
  }

  @Override
  public String name()
  {
    return "generate";
  }
}
