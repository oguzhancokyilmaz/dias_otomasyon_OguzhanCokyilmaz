@echo off
echo *** Paralel Test Çalıştırıcı ***
echo Chrome ve Firefox testleri paralel olarak başlatılıyor...

start cmd /k "mvn test -Dtest=ChromeIT"
start cmd /k "mvn test -Dtest=FirefoxIT"

echo İki test de farklı pencerelerde başlatıldı.
echo Test sonuçlarını her bir pencerede görebilirsiniz. 