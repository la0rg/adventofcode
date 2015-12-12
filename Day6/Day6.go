package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

type Grid struct {
	data [][]int
}

func NewGrid() *Grid {
	ar := make([][]int, 1000)
	for i := 0; i < 1000; i++ {
		ar[i] = make([]int, 1000)
	}
	return &Grid{data: ar}
}

func (g *Grid) Traversal(x0 int, y0 int, x1 int, y1 int, command string) {
	for x := x0; x <= x1; x++ {
		for y := y0; y <= y1; y++ {
			switch command {
			case "toggle":
				g.data[x][y] += 2
			case "on":
				g.data[x][y] += 1
			case "off":
				if g.data[x][y] > 0 {
					g.data[x][y] -= 1

				}
			}
		}
	}
}

func (g *Grid) Lights() int {
	var count int
	for _, x := range g.data {
		for _, y := range x {
			count += y
		}
	}
	return count
}

func main() {
	data, err := ioutil.ReadFile("day6data.txt")
	if err != nil {
		fmt.Println("Data file can't be read." + err.Error())
		os.Exit(1)
	}
	strs := strings.Split(string(data), "\n")
	grid := NewGrid()
	for _, str := range strs {
		grid.ParseAndExec(str)
	}
	fmt.Println(grid.Lights())
}

func (g *Grid) ParseAndExec(str string) {
	parse := strings.Split(str, " ")
	if len(parse) >= 3 {
		// turn on, turn off, toggle
		var command string
		shift := 0
		if parse[0] == "turn" {
			command = parse[1]
			shift++
		} else {
			command = parse[0]
		}
		// x0,y0
		x0y0 := strings.Split(parse[1+shift], ",")
		x0, _ := strconv.Atoi(x0y0[0])
		y0, _ := strconv.Atoi(x0y0[1])
		// through x1,y1
		x1y1 := strings.Split(parse[3+shift], ",")
		x1, _ := strconv.Atoi(x1y1[0])
		y1, _ := strconv.Atoi(x1y1[1])

		g.Traversal(x0, y0, x1, y1, command)
	}
}
