# frp-Android

[README](https://github.com/FrpcCluster/frpc-Android/blob/master/README.md) | [中文介绍](https://github.com/FrpcCluster/frpc-Android/blob/master/README_zh.md)


# Project Introduction

frp Source project [https://github.com/fatedier/frp](https://github.com/fatedier/frp)

frp Help website [https://www.frp.fun/](https://www.frp.fun/)

QQ group [222670265](https://jq.qq.com/?_wv=1027&k=5kkmkwa)


## Project demo Brief diagram

<img src="https://github.com/TelDragon/Dragon/blob/master/image/frp-Android-02.png" width="266" alt="frp-Android-02.png">   <img src="https://github.com/TelDragon/Dragon/blob/master/image/frp-Android-03.png" width="266" alt="frp-Android-03.png">   <img src="https://github.com/TelDragon/Dragon/blob/master/image/frp-Android-04.png" width="266" alt="frp-Android-04.png">

## Development instructions

* [Compilation process](https://github.com/FrpcCluster/frpc-Android/blob/master/Compile_zh.md)
* software[Android studio 3.2](http://www.android-studio.org/)
* Android, .aar Library
Frp uses gombile to implement a go code compiled into android and ios platform can directly call the sdk class library

## Android SDK usage
The sdk form provided in the Android system is a class library file with a .aar suffix. When developing, you only need to import the arr class library file into the android project.

* Import package

```java
import frpclib.Frpclib;
```

## Project logic

* Start initialization, write pre-connected server address, server port number, server toke
* Add tunnel
  * Tcp protocol, encryption/compression is optional, other required fields
  * Udp protocol, encryption/compression is optional, other required
  * Http protocol, encryption/compression is optional, custom domain name/pan-domain name is optional
  * Https protocol, encryption/compression is optional, custom domain name/pan-domain name is optional
* Tunnel list, you can copy the contents of the share list

## Support situation

* Support
  * Mobile phone free root
  * Custom add server
  * protocol tcp、udp、http、https
  * Encryption, compression
  * Dynamically add tunnels, delete tunnels (hot load)
  * Custom domain name, pan domain name
  * Sharing tunnel information
  * Background process
  * Support frps 0.13.0/0.15.0/0.16.0/0.17.0/0.18.0/0.21.0
* not support
  * Save the configuration file (re-enter the software app and re-enter the information)

  
 ## Development Plan
 
* Support for multiple frps server versions 0.13.0/0.15.0/0.16.0/0.17.0/0.18.0/0.21.0
* Support IOS Apple app [https://github.com/TelDragon/frpc-IOS](https://github.com/TelDragon/frpc-IOS)
* User platform login
* Get frps server list information, provide user server selection match
* Save configuration information
* Discard the frpc.ini startup file and execute the command directly using the "execute" function
  
## Contributing frpc-Android

Interested in getting involved? We would like to help you!

* Take a look at our [issues list](https://github.com/TelDragon/frpc-Android/issues) and consider sending a Pull Request to **dev branch**.
* If you want to add a new feature, please create an issue first to describe the new feature, as well as the implementation approach. Once a proposal is accepted, create an implementation of the new features and submit it as a pull request.
* Sorry for my poor english and improvement for this document is welcome even some typo fix.
* If you have some wonderful ideas, send email to 542867428@qq.com.

**Note: We prefer you to give your advise in [issues](https://github.com/TelDragon/frpc-Android/issues), so others with a same question can search it quickly and we don't need to answer them repeatly.**

## Donation

* If [frp-Android](https://github.com/TelDragon/frpc-Android) help you a lot, you can support us by:

<img src="https://github.com/TelDragon/Dragon/blob/master/image/WeChat.png" width="266" alt="frp-Android-02.png"> <img src="https://github.com/TelDragon/Dragon/blob/master/image/Alipay.png" width="266" alt="frp-Android-02.png">
 
* thank

[bjbjoaoa@qq.com](mailto:bjbjoaoa@qq.com) [momo@188.com](mailto:momo@188.com) [zqy1301@qq.com](mailto:zqy1301@qq.com)
