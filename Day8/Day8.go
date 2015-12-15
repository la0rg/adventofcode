/*However, it is important to realize the difference between the number of characters in the code representation of the string literal and the number of characters in the in-memory string itself.

For example:

"" is 2 characters of code (the two double quotes), but the string contains zero characters.
"abc" is 5 characters of code, but 3 characters in the string data.
"aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single, escaped quote character, for a total of 7 characters in the string data.
"\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using hexadecimal notation.
Santa's list is a file that contains many double-quoted string literals, one on each line. The only escape sequences used are \\ (which represents a single backslash), \" (which represents a lone double-quote character), and \x plus two hexadecimal characters (which represents a single character with that ASCII code).

Disregarding the whitespace in the file, what is the number of characters of code for string literals minus the number of characters in memory for the values of the strings in total for the entire file?*/

package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	file, err := os.Open("day8data.txt")
	defer file.Close()
	if err != nil {
		fmt.Errorf("Can't open file.")
		os.Exit(1)
	}
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	var inCode, inString int
	for scanner.Scan() {
		strWithQuotes := scanner.Text()
		inCode += len(strWithQuotes)
		//text, _ := strconv.Unquote(strWithQuotes) // part 1
		text := strconv.Quote(strWithQuotes) // part 2
		inString += len(text)
	}
	//fmt.Printf("Result is: %d\n", (inCode - inString)) // part 1
	fmt.Printf("Result2 is: %d\n", (inString - inCode)) // part 2
}
