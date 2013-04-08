#! /bin/sh

NDK_DIRECTORY="~/android-ndk/"
PROJECT_DIRECTORY="~/src/android/AndEngineExtensions/AndEnginePhysicsBox2DExtensionNew/"

# Run build:
cd ${PROJECT_DIRECTORY}
${NDK_DIRECTORY}ndk-build

# Clean temporary files:
rm -rf ${PROJECT_DIRECTORY}obj
find . -name gdbserver -print0 | xargs -0 rm -rf
find . -name gdb.setup -print0 | xargs -0 rm -rf