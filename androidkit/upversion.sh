# git log number
get_version()
{
    local __base=${1:-1}
    echo $((`git rev-list HEAD|wc -l` + $__base))
}
gitv=$(get_version) 
echo $gitv

# the current git log
gitcurrent=`git log -n 1 |awk '{printf ($2)}'`
giti=${gitcurrent:0:7}
echo $giti

# change Version.java to update version info.
sed -i '44s/'build[0-9]*/build-"$gitv"'/' src/com/lurencun/cfuture09/androidkit/Version.java
sed -i '48s/'.*'/	String GIT_VERSION = "'"$giti"'";/' src/com/lurencun/cfuture09/androidkit/Version.java
grep VERSION_SUFFIX -n src/com/lurencun/cfuture09/androidkit/Version.java
chmod +w src/com/lurencun/cfuture09/androidkit/Version.java

