package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io"
	"strconv"
	"strings"
	"time"
)

func main() {

	m := md5.New()
	const hash = "yzbqklnj"
	const MaxInt = int(^uint(0) >> 1)
	var res int

	start := time.Now()
	for i := 1; i < MaxInt; i++ {
		m.Reset()
		io.WriteString(m, hash+strconv.Itoa(i))
		hx := hex.EncodeToString(m.Sum(nil))
		if strings.HasPrefix(hx, "000000") {
			res = i
			break
		}
	}
	fmt.Printf("%v - %v", res, time.Since(start))
}
