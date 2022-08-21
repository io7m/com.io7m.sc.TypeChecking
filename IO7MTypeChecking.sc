/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

IO7MTypeError : MethodError
{
  var <>expected;
  var <>received;

  *new
  {
    arg receiver, expected, received;

    ^super.new(nil, receiver)
      .expected_(expected)
      .received_(received);
  }

  errorString
  {
    ^"TYPE ERROR:\n"
     ++ "Expected: " ++ this.expected ++ "\n"
     ++ "Received: " ++ this.received ++ " (" ++ this.received.class ++ ")\n";
  }
}

IO7MTypeChecking
{
  *checkExact
  {
    arg class, expression;

    if (class      == nil, { MethodError.new ("class == nil", this).throw; });
    if (expression == nil, { MethodError.new ("expression == nil", this).throw; });

    if (expression.class != class, {
      IO7MTypeError.new (IO7MTypeChecking, class, expression).throw;
    });

    ^expression;
  }

  *check
  {
    arg class, expression;

    if (class      == nil, { MethodError.new ("class == nil", this).throw; });
    if (expression == nil, { MethodError.new ("expression == nil", this).throw; });

    if (expression.isKindOf(class) == false, {
      IO7MTypeError.new (IO7MTypeChecking, class, expression).throw;
    });

    ^expression;
  }
}

IO7MTypeCheckingTest : UnitTest
{
  test_IntegerString
  {
    this.assertException({
      IO7MTypeChecking.check(Integer, "x");
    }, IO7MTypeError);
  }

  test_IntegerStringExact
  {
    this.assertException({
      IO7MTypeChecking.checkExact(Integer, "x");
    }, IO7MTypeError);
  }

  test_IntegerInteger
  {
    IO7MTypeChecking.check(Integer, 23);
  }

  test_IntegerIntegerExact
  {
    IO7MTypeChecking.checkExact(Integer, 23);
  }
}
