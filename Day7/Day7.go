/*Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.
x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.

Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.*/

package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Expr struct {
	f        func() uint16 // anonymous function that calc result of the expression
	isCashed bool
	mem      uint16 // cache for result of the expression
}

// Contains data and logic of evaluations of variables
type Scheme struct {
	Wires map[string]Expr // all connections between gates (map of variables to expressions)
}

// add new connection on the scheme
// divide @str by variable name and expression itself
func (s *Scheme) AddWire(str string) {
	tokens := strings.Split(str, " -> ")
	s.Wires[tokens[1]] = s.NewExpr(tokens[0])
}

// Get variable value by its name @variable
// If the @variable represents a number than return it like uint16
// Caching result in exrpressions
// DO NOT evaluate variables that doesn't exist in the scheme
// that will corrupt the logic
func (s *Scheme) eval(variable string) uint16 {
	if v64, err := strconv.ParseUint(variable, 10, 16); err == nil {
		return uint16(v64)
	}
	v := s.Wires[variable]
	if !v.isCashed {
		v.mem = v.f() // recursion call (f contains of eval()'s)
		v.isCashed = true
	}
	s.Wires[variable] = v
	return v.mem
}

// parse & create new expression from the string @expr
// create anonymous function that calc result of the expresion
func (s *Scheme) NewExpr(expr string) Expr {
	var a, b string
	var value uint
	switch {
	case strings.Contains(expr, "NOT"):
		fmt.Sscanf(expr, "NOT %2s", &a)
		return Expr{f: func() uint16 { return ^s.eval(a) }}
	case strings.Contains(expr, "AND"):
		fmt.Sscanf(expr, "%2s AND %2s", &a, &b)
		return Expr{f: func() uint16 { return s.eval(a) & s.eval(b) }}
	case strings.Contains(expr, "OR"):
		fmt.Sscanf(expr, "%2s OR %2s", &a, &b)
		return Expr{f: func() uint16 { return s.eval(a) | s.eval(b) }}
	case strings.Contains(expr, "LSHIFT"):
		fmt.Sscanf(expr, "%2s LSHIFT %d", &a, &value)
		return Expr{f: func() uint16 { return s.eval(a) << value }}
	case strings.Contains(expr, "RSHIFT"):
		fmt.Sscanf(expr, "%2s RSHIFT %d", &a, &value)
		return Expr{f: func() uint16 { return s.eval(a) >> value }}
	default:
		return Expr{f: func() uint16 { return s.eval(expr) }}
	}
}

func main() {
	file, err := os.Open("day7data.txt")
	defer file.Close()
	if err != nil {
		fmt.Errorf("Can't open file.")
		os.Exit(1)
	}
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	scheme := Scheme{Wires: map[string]Expr{}}
	for scanner.Scan() {
		scheme.AddWire(scanner.Text())
	}

	// part 1
	fmt.Println(scheme.eval("a"))

	/* part 2
	Now, take the signal you got on wire a, override wire b to that signal,
	and reset the other wires (including wire a). What new signal is ultimately
	provided to wire a?
	*/
	scheme.Wires["b"] = Expr{isCashed: true, mem: scheme.eval("a")}
	for v, w := range scheme.Wires {
		if v != "b" {
			w.isCashed = false
			scheme.Wires[v] = w
		}
	}
	fmt.Println(scheme.eval("a"))
}
