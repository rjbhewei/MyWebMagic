#! /bin/bash

[ -z $CHANNEL_HOME ] && CHANNEL_HOME=`cd ..;pwd`
echo "CHANNEL_HOME   : "$CHANNEL_HOME

CHANNEL_BIN=$CHANNEL_HOME/bin
CHANNEL_SERVER=$CHANNEL_HOME/server
CHANNEL_LIB=$CHANNEL_HOME/lib
CHANNEL_LOG=$CHANNEL_HOME/logs
CHANNEL_OPTS=$CHANNEL_BIN/runtime.properties

CHANNEL_PROJECT_FILE_PREFIX="channel-"
CHANNEL_PROJECT_FILE_SUFFIX="jar"

if [ ! -z "$CLASSPATH" ]; then
  CLASSPATH="$CLASSPATH"":"
fi

if [ ! -d "$CHANNEL_LOG" ]; then
  mkdir $CHANNEL_LOG
fi
if [ -z "$CHANNEL_OUT" ]; then
  CHANNEL_OUT="$CHANNEL_LOG"/channel.out
fi
touch "$CHANNEL_OUT"
> "$CHANNEL_OUT"

cd $CHANNEL_SERVER
PROJECT_JAR=`ls -l $CHANNEL_PROJECT_FILE_PREFIX*.$CHANNEL_PROJECT_FILE_SUFFIX | grep '^-' | awk '{print $9}' | sort -V | awk 'END{print $1}'`
# TODO catch error
PROJECT_NAME=${PROJECT_JAR%$".$CHANNEL_PROJECT_FILE_SUFFIX"}
if [ ! -d $CHANNEL_SERVER/$PROJECT_NAME ]; then
  echo "extract $PROJECT_JAR"
  unzip -q $CHANNEL_SERVER/$PROJECT_JAR -d $CHANNEL_SERVER/$PROJECT_NAME
fi

CLASSPATH=$CLASSPATH$CHANNEL_LIB/*:$CHANNEL_SERVER/$PROJECT_NAME

JAVA_OPTS=""
while read line; do
  if [ -n "$line" -a "${line:0:1}" != "#" ]; then
    if [ -n "$JAVA_OPTS" ]; then
      JAVA_OPTS="$JAVA_OPTS $line"
    else
      JAVA_OPTS="$line"
    fi
  fi
done < "$CHANNEL_OPTS"

echo "CHANNEL_OUT    : $CHANNEL_OUT"
echo "CLASSPATH : $CLASSPATH"
echo "JAVA_OPTS : $JAVA_OPTS"

cd $CHANNEL_HOME
exec java -Dchannel.home=$CHANNEL_HOME -Dchannel.running=$PROJECT_NAME -Dchannel.env.ver=1.0 $JAVA_OPTS -cp $CLASSPATH com.hewei.spider.main.Launcher >> "$CHANNEL_OUT" 2>&1 &
