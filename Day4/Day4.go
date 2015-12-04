package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io"
	"strconv"
	"strings"
)

func main() {
	m := md5.New()
	const hash = "yzbqklnj"
	var res int = 1
	for ; ; res++ {
		m.Reset()
		io.WriteString(m, hash+strconv.Itoa(res))
		hx := hex.EncodeToString(m.Sum(nil))
		if strings.HasPrefix(hx, "000000") {
			break
		}
	}
	fmt.Printf("%v", res)
}
