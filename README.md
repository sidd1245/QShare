# QShare

[![Java](https://img.shields.io/badge/Java-21-orange)](#requirements)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue)](https://openjfx.io/)
[![Javalin](https://img.shields.io/badge/Javalin-6.7.0-brightgreen)](https://javalin.io/)
[![Maven](https://img.shields.io/badge/build-Maven-red)](https://maven.apache.org/)
[![Windows Installer](https://img.shields.io/badge/installer-jpackage-success)](#build-a-windows-installer-locally)
[![Releases](https://img.shields.io/badge/download-GitHub%20Releases-black)](#github-releases)

QShare is a desktop file sharing app built with JavaFX and Javalin. It starts a local web server, shows a QR code for the server URL, and lets devices on the same network upload and download files through a browser.

## Features

- JavaFX desktop interface
- QR code for quick phone access
- Embedded Javalin web server
- Multiple file upload support
- Uploaded files stored in the user profile at `C:\Users\<username>\QShare\uploads`
- Windows installer support through `jpackage`

## Requirements

- JDK 21
- Maven
- Windows, for building the `.exe` installer
- WiX Toolset, required by `jpackage` when creating Windows installers

## Run From Source

```powershell
mvn javafx:run
```

## Build The App

```powershell
mvn clean package
```

The shaded JAR is created at:

```text
target\file_Share-1.0-SNAPSHOT.jar
```

## Build A Windows Installer Locally

Build the JAR first:

```powershell
mvn clean package
```

Prepare the `jpackage` input folder:

```powershell
New-Item -ItemType Directory -Force -Path dist
Copy-Item target\file_Share-1.0-SNAPSHOT.jar dist\file_Share-1.0-SNAPSHOT.jar -Force
```

Create the installer:

```powershell
jpackage `
  --input dist `
  --dest target\installer `
  --name QShare `
  --app-version 1.0.1 `
  --win-upgrade-uuid 0d3e75fc-71c5-4b87-9d2e-19f81a9f1e44 `
  --main-jar file_Share-1.0-SNAPSHOT.jar `
  --main-class ui.Launcher `
  --type exe `
  --icon src\main\resources\icons\app.ico `
  --win-menu `
  --win-shortcut
```

The installer will be created under:

```text
target\installer
```

Do not commit the generated `.exe`; publish it through GitHub Releases.

## GitHub Releases

This repository includes a GitHub Actions workflow that builds the Windows installer.

To publish a release:

```powershell
git tag v1.0.1
git push origin v1.0.1
```

When the tag is pushed, GitHub Actions builds `QShare-1.0.1.exe` and attaches it to the GitHub Release.

## Runtime Files

QShare creates its working folder here:

```text
C:\Users\<username>\QShare
```

Inside it:

- `uploads` contains received files
- `refresh.log` records refresh attempts
- `error.log` records refresh errors, if any
- `crash.log` records server startup crashes, if any
