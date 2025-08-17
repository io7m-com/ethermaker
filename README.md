ethermaker
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.ethermaker/com.io7m.ethermaker.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.ethermaker%22)
[![Maven Central (snapshot)](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fcom%2Fio7m%2Fethermaker%2Fcom.io7m.ethermaker%2Fmaven-metadata.xml&style=flat-square)](https://central.sonatype.com/repository/maven-snapshots/com/io7m/ethermaker/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/ethermaker.svg?style=flat-square)](https://codecov.io/gh/io7m-com/ethermaker)
![Java Version](https://img.shields.io/badge/21-java?label=java&color=e6c35c)

![com.io7m.ethermaker](./src/site/resources/ethermaker.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/ethermaker/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/ethermaker/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/ethermaker/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/ethermaker/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/ethermaker/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/ethermaker/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/ethermaker/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/ethermaker/actions?query=workflow%3Amain.windows.temurin.lts)|

## ethermaker

The `ethermaker` package implements a set of tools and APIs for
generating [MAC addresses](https://en.wikipedia.org/wiki/MAC_address).

## Features

* Generates MAC addresses.
* Describes MAC addresses.
* Written in pure Java 21 for safety and portability.
* High coverage test suite (100%, minus an unreachable private constructor).
* [OSGi-ready](https://www.osgi.org/)
* [JPMS-ready](https://en.wikipedia.org/wiki/Java_Platform_Module_System)
* ISC license.

## Usage

```
Usage: ethermaker [options] [command] [command options]

  Options:
    --verbose
      Set the minimum logging verbosity level.
      Default: info
      Possible Values: [trace, debug, info, warn, error]

  Use the "help" command to examine specific commands:

    $ ethermaker help help.

  Command-line arguments can be placed one per line into a file, and the file
  can be referenced using the @ symbol:

    $ echo help > file.txt
    $ echo help >> file.txt
    $ ethermaker @file.txt

  Commands:
    describe     Describe MAC addresses
    generate     Generate MAC addresses
    help         Show detailed help messages for commands.
    version      Show the application version.

  Documentation:
    https://www.io7m.com/software/ethermaker/
```

To generate a set of ten random unicast MAC addresses:

```
$ ethermaker generate --count 10
68:cb:e4:d4:2d:94
22:c5:8c:99:22:15
8a:2c:2b:cd:71:98
5e:6e:8b:da:a3:00
dc:ab:be:0b:5f:14
d0:24:40:33:53:db
0a:2f:4e:20:eb:11
8c:95:69:ac:42:d2
32:9c:da:f3:a6:c8
f4:de:7f:4d:4e:5b
```

To generate a set of ten random MAC addresses under the organization `C419D1`:

```
$ ethermaker generate --count 10 --organization C419D1
c4:19:d1:39:d9:e3
c4:19:d1:f7:80:95
c4:19:d1:f8:d0:73
c4:19:d1:ef:b6:16
c4:19:d1:1e:60:db
c4:19:d1:25:91:23
c4:19:d1:7f:59:d1
c4:19:d1:60:30:0d
c4:19:d1:3e:78:eb
c4:19:d1:a5:f1:cb
```

To describe a set of MAC addresses:

```
$ ethermaker generate --count 10 --organization C419D1 | ethermaker describe
Address: c4:19:d1:83:97:9b, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:8f:84:77, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:50:af:bb, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:4a:f2:1e, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:97:30:58, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:d9:d7:a3, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:53:d2:98, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:ee:f3:d2, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:43:c0:41, Multicast: false, Broadcast: false, Local: false
Address: c4:19:d1:e3:53:89, Multicast: false, Broadcast: false, Local: false
```

