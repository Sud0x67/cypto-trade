/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.enums;

public enum TimeInForce {
  GTC, //	Good Til Canceled  An order will be on the book unless the order is canceled.
  IOC, //	Immediate Or Cancel  An order will try to fill the order as much as it can before the
  // order expires.
  FOK //	Fill or KillAn order will expire if the full order cannot be filled upon execution.
}
