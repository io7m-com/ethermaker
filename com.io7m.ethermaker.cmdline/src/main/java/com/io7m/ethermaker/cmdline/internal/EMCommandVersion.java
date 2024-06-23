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

import java.util.Properties;

import static com.io7m.claypot.core.CLPCommandType.Status.SUCCESS;

/**
 * The "version" command.
 */

@Parameters(commandDescription = "Show the application version.")
public final class EMCommandVersion extends CLPAbstractCommand
{
  /**
   * Construct a command.
   *
   * @param inContext The command context
   */

  public EMCommandVersion(
    final CLPCommandContextType inContext)
  {
    super(inContext);
  }

  @Override
  protected Status executeActual()
    throws Exception
  {
    final var url =
      EMCommandVersion.class.getResource(
        "/com/io7m/ethermaker/cmdline/internal/version.properties"
      );

    try (var stream = url.openStream()) {
      final var properties = new Properties();
      properties.load(stream);
      System.out.printf("ethermaker %s%n", properties.getProperty("version"));
    }
    return SUCCESS;
  }

  @Override
  public String name()
  {
    return "version";
  }
}
