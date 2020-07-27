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

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveI;
import org.immutables.value.Value;

/**
 * A MAC address.
 */

@ImmutablesStyleType
@Value.Immutable
public abstract class MACAddressType
{
  private static final RangeInclusiveI OCTET_RANGE =
    RangeInclusiveI.of(0, 255);

  /**
   * @return Octet 0
   */

  abstract int octet0();

  /**
   * @return Octet 1
   */

  abstract int octet1();

  /**
   * @return Octet 2
   */

  abstract int octet2();

  /**
   * @return Octet 3
   */

  abstract int octet3();

  /**
   * @return Octet 4
   */

  abstract int octet4();

  /**
   * @return Octet 5
   */

  abstract int octet5();

  /**
   * @return The organization identifier
   */

  public final String organization()
  {
    return String.format(
      "%02x%02x%02x",
      Integer.valueOf(this.octet0()),
      Integer.valueOf(this.octet1()),
      Integer.valueOf(this.octet2())
    );
  }

  /**
   * @return {@code true} if this address is multicast, or {@code false} if it is unicast
   */

  public final boolean isMulticast()
  {
    return (this.octet0() & 0b0000_0001) == 0b0000_0001;
  }

  /**
   * @return {@code true} if this address is locally administered, or {@code false} if it is OUI enforced
   */

  public final boolean isLocallyAdministered()
  {
    return (this.octet0() & 0b0000_0010) == 0b0000_0010;
  }

  /**
   * @return {@code true} if this address is a broadcast address
   */

  public final boolean isBroadcast()
  {
    boolean ok = this.octet0() == 0b1111_1111;
    ok &= this.octet1() == 0b1111_1111;
    ok &= this.octet2() == 0b1111_1111;
    ok &= this.octet3() == 0b1111_1111;
    ok &= this.octet4() == 0b1111_1111;
    ok &= this.octet5() == 0b1111_1111;
    return ok;
  }

  /**
   * Check preconditions for the type.
   */

  @Value.Check
  protected final void checkPreconditions()
  {
    RangeCheck.checkIncludedInInteger(
      this.octet0(),
      "Octet 0",
      OCTET_RANGE,
      "Valid octet values");
    RangeCheck.checkIncludedInInteger(
      this.octet1(),
      "Octet 1",
      OCTET_RANGE,
      "Valid octet values");
    RangeCheck.checkIncludedInInteger(
      this.octet2(),
      "Octet 2",
      OCTET_RANGE,
      "Valid octet values");
    RangeCheck.checkIncludedInInteger(
      this.octet3(),
      "Octet 3",
      OCTET_RANGE,
      "Valid octet values");
    RangeCheck.checkIncludedInInteger(
      this.octet4(),
      "Octet 4",
      OCTET_RANGE,
      "Valid octet values");
    RangeCheck.checkIncludedInInteger(
      this.octet5(),
      "Octet 5",
      OCTET_RANGE,
      "Valid octet values");
  }

  @Override
  public final String toString()
  {
    return String.format(
      "%02x:%02x:%02x:%02x:%02x:%02x",
      Integer.valueOf(this.octet0()),
      Integer.valueOf(this.octet1()),
      Integer.valueOf(this.octet2()),
      Integer.valueOf(this.octet3()),
      Integer.valueOf(this.octet4()),
      Integer.valueOf(this.octet5())
    );
  }
}
