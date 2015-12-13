package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type Expr struct {
	f func() uint16
}

type Scheme struct {
	Wires map[string]Expr
}

func (s *Scheme) AddWire(str string) {
	tokens := strings.Split(str, " -> ")
	s.Wires[tokens[1]] = s.NewExpr(str)
}

func (s *Scheme) eval(variable string) uint16 {
	return s.Wires[variable].f()
}

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
		var v uint16
		fmt.Sscanf(expr, "%d", &v)
		return Expr{f: func() uint16 { return v }}
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
	fmt.Println(scheme)
	fmt.Println(scheme.eval("z"))
}
