package main

import "testing"

func TestRibbon(t *testing.T) {
	rect := Rectangular{2, 3, 4}
	if rect.Ribbon() != 34 {
		t.Errorf("Rectangular{2,3,4} ribbon lenth is not equal to 34, but: %d", rect.Ribbon())
	}
}
