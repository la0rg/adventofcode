/*Every year, Santa manages to deliver all of his presents in a single night.

This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?

For example, given the following distances:

London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
The possible routes are therefore:

Dublin -> London -> Belfast = 982
London -> Dublin -> Belfast = 605
London -> Belfast -> Dublin = 659
Dublin -> Belfast -> London = 659
Belfast -> Dublin -> London = 605
Belfast -> London -> Dublin = 982
The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.

What is the distance of the shortest route?*/

package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	file, err := os.Open("day9data.txt")
	defer file.Close()
	if err != nil {
		fmt.Errorf("Can't open file.")
		os.Exit(1)
	}
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	var a, b string
	var c int
	g := make(map[string]map[string]int) // kinda adjacency matrix
	// fill the map
	for scanner.Scan() {
		if _, err := fmt.Sscanf(scanner.Text(), "%s to %s = %d", &a, &b, &c); err == nil {
			addToMap(g, a, b, c)
			addToMap(g, b, a, c)
		} else {
			fmt.Println("ERR " + err.Error())
		}
	}

	// start from every city ( by using kinda hungry algorithm ) and choose smallest one
	// EDIT: choose the smallest or highest rely on @isSmallest
	var smallestDistance int
	for cityName := range g {
		d := Distance(g, cityName, true)
		if d < smallestDistance || smallestDistance == 0 {
			smallestDistance = d
		}
	}
	fmt.Printf("SMALLEST PATH IS: %d\n", smallestDistance)
	var biggestDistance int
	for cityName := range g {
		d := Distance(g, cityName, false)
		if d > biggestDistance || biggestDistance == 0 {
			biggestDistance = d
		}
	}
	fmt.Printf("BIGGEST PATH IS: %d\n", biggestDistance)

}

// measure distance starts from specified city to other using hungry method
func Distance(mp map[string]map[string]int, cityName string, isSmallest bool) (distance int) {
	passed := make([]string, 0) // there's will probably lead to too many allocations
	passed = append(passed, cityName)
	for len(passed) < len(mp) {
		nextCity, length := hungryChoose(mp, cityName, &passed, isSmallest)
		distance += length
		cityName = nextCity
	}
	return
}

// add value to the map
func addToMap(mp map[string]map[string]int, a string, b string, c int) {
	m, ok := mp[a]
	if !ok {
		m = make(map[string]int)
		mp[a] = m
	}
	m[b] = c
}

// find the next city to visit by smallest distance avoid passed city
func hungryChoose(mp map[string]map[string]int, from string, passed *[]string, isSmallest bool) (to string, distance int) {
CityLoop:
	for city, d := range mp[from] {
		// do not go to the city that is already visited
		for _, p := range *passed {
			if city == p {
				continue CityLoop
			}
		}
		if isSmallest {
			// first or choose the smallest one
			if d < distance || distance == 0 {
				distance = d
				to = city
			}
		} else {
			if d > distance || distance == 0 {
				distance = d
				to = city
			}
		}
	}
	*passed = append(*passed, to)
	return
}
