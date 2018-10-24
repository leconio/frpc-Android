// Copyright 2016 fatedier, fatedier@gmail.com
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
	"os"
	"os/signal"
	"syscall"
	"time"

	ini "github.com/vaughan0/go-ini"

	"github.com/fatedier/frp/client"
	"github.com/fatedier/frp/models/config"
	"github.com/fatedier/frp/utils/log"
	//"bytes"
)

func Run(confFile string){
	var err error
	var conf ini.File
	//data, err := Asset("config.ini")
	if err != nil {
		fmt.Printf("load ini err")
	}
	//iniReader:=bytes.NewReader(data)
	//conf, err = ini.Load(iniReader)
	conf, err = ini.LoadFile(confFile)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	config.ClientCommonCfg, err = config.LoadClientCommonConf(conf)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	config.ClientCommonCfg.ConfigFile = confFile

	pxyCfgs, vistorCfgs, err := config.LoadProxyConfFromFile(config.ClientCommonCfg.User, conf, config.ClientCommonCfg.Start)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}

	log.InitLog(config.ClientCommonCfg.LogWay, config.ClientCommonCfg.LogFile,
		config.ClientCommonCfg.LogLevel, config.ClientCommonCfg.LogMaxDays)

	svr := client.NewService(pxyCfgs, vistorCfgs)

	// Capture the exit signal if we use kcp.
	if config.ClientCommonCfg.Protocol == "kcp" {
		go HandleSignal(svr)
	}

	err = svr.Run()
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
}

func HandleSignal(svr *client.Service) {
	ch := make(chan os.Signal)
	signal.Notify(ch, syscall.SIGINT, syscall.SIGTERM)
	<-ch
	svr.Close()
	time.Sleep(250 * time.Millisecond)
	os.Exit(0)
}