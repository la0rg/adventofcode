// package meant to be concise version of the Day9
// also use all permutations instead of hacky decision on Day9
// all error handling was omitted to cut size

package main

import (
	"bufio"
	"fmt"
	"os"
)

// Key type for the graph map
type Key struct {
	from string
	to   string
}

func main() {
	// open file and fill map
	file, _ := os.Open("day9data.txt")
	defer file.Close()
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	a, b, c := "", "", 0
	g := make(map[Key]int)          // kinda adjacency matrix
	cities := make(map[string]bool) // set of cities
	for scanner.Scan() {
		fmt.Sscanf(scanner.Text(), "%s to %s = %d", &a, &b, &c)
		g[Key{a, b}] = c
		g[Key{b, a}] = c
		cities[a] = true
		cities[b] = true
	}
	fmt.Printf("Min: %d\n", FindDistance(g, cities, true))
	fmt.Printf("Max: %d\n", FindDistance(g, cities, false))
}

// Take graph map and set of cities to find min(max) path from one city to another with visit each of them
func FindDistance(g map[Key]int, cities map[string]bool, isMin bool) (value int) {
	// sums up distances of all permutations and choose min (max)
	keys := make([]string, len(cities))
	i := 0
	for k := range cities {
		keys[i] = k
		i++
	}
	distances := []int{}
	for perm := range Permutations(keys) {
		sum := 0
		for i := 0; i < len(perm)-1; i++ {
			sum += g[Key{perm[i], perm[i+1]}]
		}
		distances = append(distances, sum)
	}
	for _, d := range distances {
		if (value == 0) || (isMin && d < value) || (!isMin && d > value) {
			value = d
		}
	}
	return
}

// Generate all permutations of variables for the given array s
// Meant to be used in range syntax
func Permutations(s []string) <-chan []string {
	c := make(chan []string)
	go func() {
		defer close(c)
		generate(len(s), s, c)
	}()
	return c
}

// Recursion func that make permutations
func generate(n int, a []string, c chan []string) {
	if n == 1 {
		c <- a
	} else {
		for i := 0; i < n-1; i++ {
			generate(n-1, a, c)
			if n%2 == 0 {
				a[i], a[n-1] = a[n-1], a[i]
			} else {
				a[0], a[n-1] = a[n-1], a[0]
			}
		}
		generate(n-1, a, c)
	}
}
