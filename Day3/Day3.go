package main

import (
	"fmt"
	"io/ioutil"
	"os"
)

type house struct {
	X int
	Y int
}

func main() {
	data, err := ioutil.ReadFile("day3data.txt")
	if err != nil {
		fmt.Errorf("Data file can't be read.")
		os.Exit(1)
	}

	var x, y int
	houses := make(map[house]bool)
	houses[house{x, y}] = true // start at (0,0) coordinates
	for _, val := range data {
		switch string(val) {
		case "^":
			y++
			houses[house{x, y}] = true
		case "v":
			y--
			houses[house{x, y}] = true
		case ">":
			x++
			houses[house{x, y}] = true
		case "<":
			x--
			houses[house{x, y}] = true
		}
	}
	fmt.Println(len(houses))
}
