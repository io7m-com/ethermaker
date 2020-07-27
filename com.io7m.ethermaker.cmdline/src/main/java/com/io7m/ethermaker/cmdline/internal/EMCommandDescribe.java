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

import com.beust.jcommander.Parameters;
import com.io7m.claypot.core.CLPAbstractCommand;
import com.io7m.claypot.core.CLPCommandContextType;
import com.io7m.ethermaker.core.MACAddresses;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.io7m.claypot.core.CLPCommandType.Status.SUCCESS;

@Parameters(commandDescription = "Describe MAC addresses")
public final class EMCommandDescribe extends CLPAbstractCommand
{
  /**
   * Construct a command.
   *
   * @param inContext The command context
   */

  public EMCommandDescribe(
    final CLPCommandContextType inContext)
  {
    super(inContext);
  }

  @Override
  public String extendedHelp()
  {
    return EMMessages.create()
      .format("helpDescribe");
  }

  @Override
  protected Status executeActual()
    throws Exception
  {
    final var reader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      final var line = reader.readLine();
      if (line == null) {
        break;
      }
      final var text = line.trim();
      if (text.isEmpty()) {
        continue;
      }

      try {
        final var address = MACAddresses.parse(text);
        System.out.printf(
          "Address: %s, Multicast: %s, Broadcast: %s, Local: %s%n",
          address,
          Boolean.valueOf(address.isMulticast()),
          Boolean.valueOf(address.isBroadcast()),
          Boolean.valueOf(address.isLocallyAdministered())
        );
      } catch (final Exception e) {
        this.logger().error("Failed to parse address: ", e);
      }
    }

    return SUCCESS;
  }

  @Override
  public String name()
  {
    return "describe";
  }
}
