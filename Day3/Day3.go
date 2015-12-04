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

type santa struct {
	position      house
	visitedHouses map[house]bool
}

func NewSanta(houses map[house]bool) *santa {
	s := &santa{visitedHouses: houses, position: *new(house)}
	s.visitedHouses[s.position] = true
	return s
}

func (s *santa) GoTo(direction string) {
	switch direction {
	case "^":
		s.position.Y++
	case "v":
		s.position.Y--
	case ">":
		s.position.X++
	case "<":
		s.position.X--
	}
	s.visitedHouses[s.position] = true
}

func (s *santa) CountOfVisitedHouses() int {
	return len(s.visitedHouses)
}

func main() {
	data, err := ioutil.ReadFile("day3data.txt")
	if err != nil {
		fmt.Errorf("Data file can't be read.")
		os.Exit(1)
	}

	houses := make(map[house]bool)
	realSanta := NewSanta(houses)
	roboSanta := NewSanta(houses)
	for i, val := range data {
		if i%2 != 0 {
			realSanta.GoTo(string(val))
		} else {
			roboSanta.GoTo(string(val))
		}
	}
	fmt.Println(realSanta.CountOfVisitedHouses())
}
