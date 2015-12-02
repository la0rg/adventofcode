package main

import (
	"fmt"
	"io/ioutil"
	"os"
)

func main() {
	data, err := ioutil.ReadFile("data.txt")
	if err != nil {
		fmt.Errorf("Data file can't be read.")
		os.Exit(1)
	}
	floor, basement := FloorAndBasement(data)
	fmt.Printf("First enter to the basement: %d\n", basement)
	fmt.Printf("Result floor is: %d\n", floor)

}

func FloorAndBasement(input []byte) (floor int, base int) {
	basement := false
	for i, s := range input {
		switch string(s) {
		case "(":
			floor++
		case ")":
			floor--
		}
		if !basement && floor <= -1 {
			basement = true
			base = i + 1
		}
	}
	return
}
