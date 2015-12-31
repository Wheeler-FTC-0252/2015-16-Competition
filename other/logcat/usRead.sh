today=$(date "+%Y.%m.%d-%H.%M.%S")
adb logcat -v raw -s "distance" | tee ~/Desktop/$(date "+%Y.%m.%d-%H.%M.%S")-us.txt

#adb logcat -v raw -s "distanceWarn"
