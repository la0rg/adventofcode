package main

import (
	"errors"
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

type Rectangular struct {
	length, width, height int
}

func (r *Rectangular) Area() int {
	lw := r.length * r.width
	wh := r.width * r.height
	hl := r.height * r.length
	return 2*(lw+wh+hl) + min(lw, wh, hl)
}

func (r *Rectangular) Ribbon() int {
	lw := 2*r.length + 2*r.width
	wh := 2*r.width + 2*r.height
	hl := 2*r.height + 2*r.length
	return min(lw, wh, hl) + r.length*r.width*r.height
}

func main() {
	data, err := ioutil.ReadFile("day2data.txt")
	if err != nil {
		fmt.Errorf("Data file can't be read.")
		os.Exit(1)
	}
	lines := strings.Split(string(data), "\r\n")
	var sum = 0
	var ribbon = 0
	for _, box := range lines {
		params, err := parseParams(box)
		if err != nil {
			continue
		} //just ignore lines with corrupt data
		rect := &Rectangular{length: params[0], width: params[1], height: params[2]}
		sum += rect.Area()
		ribbon += rect.Ribbon()
	}
	fmt.Println(sum)
	fmt.Println(ribbon)
}

func parseParams(str string) ([]int, error) {
	params := strings.Split(str, "x")
	result := make([]int, 0)
	for _, param := range params {
		value, err := strconv.Atoi(param)
		if err != nil {
			return []int{-1}, errors.New("Exception on parsing:" + err.Error())
		}
		result = append(result, value)
	}
	return result, nil
}

func min(nums ...int) int {
	if len(nums) != 0 {
		min := nums[0]
		for _, num := range nums {
			if num < min {
				min = num
			}
		}
		return min
	}
	return -1
}
