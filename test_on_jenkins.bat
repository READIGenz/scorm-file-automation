echo "Parameter 0 : %0"
echo "Parameter 1 : %1"

mvn test -Dtest=JunitRunner -Dsystem="jenkins" -Dcucumber.options="--tags %1"