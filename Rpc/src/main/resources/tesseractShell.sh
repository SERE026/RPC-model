#!/bin/sh
# ./startup.sh <jarname> <start|stop>

dir=${0%/*}

lib=${dir}/lib
jar='.'

cd ${dir}

for i in `ls $lib`; do 
jar=$jar:$lib/$i
done

DATE=$(date +%Y-%m-%d)

echo $DATE

name=$1
. ../setEnv.sh
export CLASSPATH=$CLASSPATH:${name}.jar:${jar}
#export LANG=zh_CN.gbk

echo $CLASSPATH

case $2 in
  start)
    #if [ -f ${dir}/pid/${name}.pid ];then
    #  echo 'aabb already started or pid already exsit'
    #else 

    echo "java -Xms64M -Xmx128M  -Dfile.encoding=GBK -classpath $CLASSPATH com.ocr.TesseractDemo"
    java -Xms64M -Xmx512M  -Dfile.encoding=utf8 -classpath $CLASSPATH com.ocr.TesseractDemo /deploy/server/ycm_jobs/Tess4jDemo/tess4j.png  
   # sleep 3
   # echo `ps -ef  | grep java | grep ${name}.jar | grep -v grep | awk '{print $2}'` > ${dir}/pid/${name}.pid
    #fi
      ;;
  stop)
   # kill -9 `cat ${dir}/pid/${name}.pid`
   # rm -f ${dir}/pid/${name}.pid
      ;;
     *)
      echo 'usage: test.sh jarname start or stop'
esac
exit 0