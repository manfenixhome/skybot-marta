#!/bin/sh

# Configuration
SERVER_CONF_SOURCE=/opt/source/skybot-marta/server-conf
SOURCE_ROOT=/opt/source/skybot-marta
GIT_REPOSITORY=$SOURCE_ROOT/.git
GIT_REPOSITORY_FILES=$SOURCE_ROOT

# Splash
echo "####  #   # #     #        #   #  ###  ####  #####  ### "
echo "#   # #   # #     #        ## ## #   # #   #   #   #   #"
echo "####  #   # #     #        # # # ##### ####    #   #####"
echo "#     #   # #     #        #   # #   # #   #   #   #   #"
echo "#      ###  ##### #####    #   # #   # #   #   #   #   #"

# Check parameters
if [ "$#" -lt 1 ]
then
    echo Usage: mbm-pull-mba \<branch_name\>
    echo Deploying requires that you give the branch name
    echo as an option.
    exit 1
fi

# Pull changes
git --work-tree=$GIT_REPOSITORY_FILES --git-dir=$GIT_REPOSITORY pull --all
git --work-tree=$GIT_REPOSITORY_FILES --git-dir=$GIT_REPOSITORY checkout $1
git --work-tree=$GIT_REPOSITORY_FILES --git-dir=$GIT_REPOSITORY pull

cp $SERVER_CONF_SOURCE/marta-deploy.sh /opt/bin/marta-deploy
cp $SERVER_CONF_SOURCE/marta-pull.sh /opt/bin/marta-pull
cp $SERVER_CONF_SOURCE/playd-marta.sh /etc/init.d/play-marta
cp $SERVER_CONF_SOURCE/marta-prod-logback.xml /opt/conf/play/marta-prod-logback.xml
cp $SERVER_CONF_SOURCE/marta-extended.conf /opt/conf/play/marta-extended.conf

echo "Copying live server configuration"
cp $SERVER_CONF_SOURCE/live_application.conf /opt/conf/play/marta-application.conf

chmod 700 /opt/bin/marta*
