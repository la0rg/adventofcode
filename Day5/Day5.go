package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

var vowels = []string{"a", "e", "i", "o", "u"}

func main() {
	data, err := ioutil.ReadFile("day5data.txt")
	if err != nil {
		fmt.Println("Data file can't be read." + err.Error())
		os.Exit(1)
	}
	arr := strings.Split(string(data), "\n")
	processing(arr, isNice)                                                                      // part1
	processing(arr, func(str string) bool { return findPairs(str) && singleLetterBetween(str) }) // part2
}

func processing(arr []string, f func(str string) bool) {
	count := 0 // how many are nice?
	for _, s := range arr {
		if f(s) {
			count++
		}
	}
	fmt.Println(count)
}

func isNice(str string) bool {
	var countVowels int
	var twiceInARow = false
	for i, s := range str {
		if isVowel(string(s)) {
			countVowels++
		} // It contains at least three vowels
		if i > 0 && !twiceInARow { // It contains at least one letter that apppears twice in a row
			if string(s) == string(str[i-1]) {
				twiceInARow = true
			}
		}
		if i > 0 && isNearby(rune(str[i-1]), rune(s)) {
			return false
		}
	}
	return countVowels >= 3 && twiceInARow
}

func isNiceBetter(str string) {
	return
}

type IntToArray struct {
	count     int
	positions []int
}

func findPairs(str string) bool {
	var pairs map[string]*IntToArray
	pairs = make(map[string]*IntToArray)

	// Build pairs
	for i := 1; i < len(str); i++ {
		s := str[i-1 : i+1]
		pair, ok := pairs[s]
		if !ok {
			pair = new(IntToArray)
			pairs[s] = pair
		}
		pair.count += 1
		pair.positions = append(pair.positions, i-1)
	}

	// Search pairs
	for _, itoa := range pairs {
		if itoa.count > 1 {
			// check distance between first symbols
			for _, pos := range itoa.positions {
				for _, pos2 := range itoa.positions {
					if pos2-pos >= 2 {
						return true
					}
				}
			}

		}
	}
	return false
}

func singleLetterBetween(str string) bool {
	for i := 0; i < len(str)-2; i++ {
		if str[i] == str[i+2] {
			return true
		}
	}
	return false
}

func isVowel(s string) bool {
	for _, v := range vowels {
		if v == s {
			return true
		}
	}
	return false
}

func isNearby(s1 rune, s2 rune) bool {
	return (s1 == 97 || s1 == 99 || s1 == 112 || s1 == 120) && s1+1 == s2
}
