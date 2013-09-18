get_version()
{
    local __base=${1:-1}
    echo $((`git rev-list HEAD|wc -l` + $__base))
}
gitv=$(get_version) 

echo $gitv
sed -i '45s/'build[0-9]*/build-"$gitv"'/' src/com/lurencun/cfuture09/androidkit/Version.java
grep VERSION_SUFFIX -n src/com/lurencun/cfuture09/androidkit/Version.java
chmod +w src/com/lurencun/cfuture09/androidkit/Version.java
