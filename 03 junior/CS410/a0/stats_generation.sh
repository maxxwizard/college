# Matthew Huynh
# CS410 - West
# February 16, 2010
# Automatic stats file generation script for a0.
# This script will do everything but the averages...

echo "----------------------" >> stats
echo ". Matthew Huynh      ." >> stats
echo ". CS410 - West       ." >> stats
echo ". February 16, 2010  ." >> stats
echo ". stats for a0       ." >> stats
echo -e "----------------------\n" >> stats
echo "header printed!" > 1

echo "============================" >> stats
echo "getsections_dl + RTLD_LAZY = ____s average" >> stats
echo "============================" >> stats
for i in {1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50}
do
	$HOME/a0/getsections_dl hw RTLD_LAZY > /dev/null
done

echo -e "\n============================" >> stats
echo "getsections_dl + RTLD_NOW = ____s average" >> stats
echo "============================" >> stats
for i in {1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50}
do
	$HOME/a0/getsections_dl hw RTLD_NOW > /dev/null
done

echo -e "\n============================" >> stats
echo "getsyms_dl + RTLD_LAZY = ____s average" >> stats
echo "============================" >> stats
for i in {1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50}
do
	$HOME/a0/getsyms_dl hw RTLD_LAZY > /dev/null
done

echo -e "\n============================" >> stats
echo "getsyms_dl + RTLD_NOW = ____s average" >> stats
echo "============================" >> stats
for i in {1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50}
do
	$HOME/a0/getsyms_dl hw RTLD_NOW > /dev/null
done
