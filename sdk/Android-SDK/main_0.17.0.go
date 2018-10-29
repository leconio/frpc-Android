// Copyright 2018 fatedier, fatedier@gmail.com
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package frpclib

import (
	"fmt"
	"io/ioutil"
	"os"
	"os/signal"
	"syscall"
	"time"

	ini "github.com/vaughan0/go-ini"

	"github.com/fatedier/frp/client"
	"github.com/fatedier/frp/g"
	"github.com/fatedier/frp/models/config"
	"github.com/fatedier/frp/utils/log"
)

//const (
//	CfgFileTypeIni = iota
//)
//var (
//	cfgFile     string
//)


//func parseClientCommonCfg(fileType int, filePath string) (err error) {
//	if fileType == CfgFileTypeIni {
//		err = parseClientCommonCfgFromIni(filePath)
//	}
//	if err != nil {
//		return err
//	}
//
//	g.GlbClientCfg.CfgFile = cfgFile
//
//	err = g.GlbClientCfg.ClientCommonConf.Check()
//	if err != nil {
//		return err
//	}
//	return
//}

//func parseClientCommonCfgFromIni(filePath string) (err error) {
//	b, err := ioutil.ReadFile(filePath)
//	if err != nil {
//		return err
//	}
//	content := string(b)
//
//	cfg, err := config.UnmarshalClientConfFromIni(&g.GlbClientCfg.ClientCommonConf, content)
//	if err != nil {
//		return err
//	}
//	g.GlbClientCfg.ClientCommonConf = *cfg
//	return
//}


func Run(cfgFile string){
	var err error
	var conf ini.File
	if err != nil {
		fmt.Printf("load ini err")
	}

	b, err := ioutil.ReadFile(cfgFile)
	if err != nil {
		fmt.Println(err)
	}
	content := string(b)

	cfg, err := config.UnmarshalClientConfFromIni(&g.GlbClientCfg.ClientCommonConf, content)
	if err != nil {
		fmt.Println(err)
	}
	g.GlbClientCfg.ClientCommonConf = *cfg

	conf, err = ini.LoadFile(cfgFile)
        if err != nil {
                fmt.Printf("load ini err")
        }

	g.GlbClientCfg.CfgFile = cfgFile


	pxyCfgs, visitorCfgs, err := config.LoadProxyConfFromIni(g.GlbClientCfg.User, conf, g.GlbClientCfg.Start)
        if err != nil {
                fmt.Println(err)
        }


	log.InitLog(g.GlbClientCfg.LogWay, g.GlbClientCfg.LogFile, g.GlbClientCfg.LogLevel, g.GlbClientCfg.LogMaxDays)

	svr := client.NewService(pxyCfgs, visitorCfgs)

	// Capture the exit signal if we use kcp.
        if g.GlbClientCfg.Protocol == "kcp" {
                go handleSignal(svr)
        }

        err = svr.Run()
        if err != nil {
		fmt.Println(err)
	}

}

func handleSignal(svr *client.Service) {
        ch := make(chan os.Signal)
        signal.Notify(ch, syscall.SIGINT, syscall.SIGTERM)
        <-ch
        svr.Close()
        time.Sleep(250 * time.Millisecond)
//        os.Exit(0)
}
