#!/bin/sh

EXECUTABLE=/opt/source/skybot-marta/target/universal/stage/bin/skybot-marta
PID_FILE=/opt/source/skybot-marta/target/universal/stage/RUNNING_PID
PID=`cat $PID_FILE`
BUILD_DIR=/opt/source/skybot-marta
BUILD_CMD=activator
CWD=`pwd`

# Splash
echo "####  ##### ####  #      ###  #   #    #   #  ###  ####  #####  ### "
echo "#   # #     #   # #     #   # #   #    ## ## #   # #   #   #   #   #"
echo "#   # ####  ####  #     #   #  ###     # # # ##### ####    #   #####"
echo "#   # #     #     #     #   #   #      #   # #   # #   #   #   #   #"
echo "####  ##### #     #####  ###    #      #   # #   # #   #   #   #   #"

# Stop the running application

/etc/init.d/play-marta stop

# Build the app

cd ${BUILD_DIR}
echo "Building app with 'activator clean compile stage'"
activator clean compile stage

# Copy logfile configuration

cp $BUILD_DIR/server-conf/marta-prod-logback.xml /opt/conf/play/marta-prod-logback.xml

# Start the server

/etc/init.d/play-marta start

# Return to previous path
cd ${CWD}








