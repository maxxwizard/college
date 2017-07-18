#!/usr/bin/perl -w

# Matthew Huynh
# CS410 - West
# Assignment 3

#$DEBUG = 1;

if ($#ARGV+1 != 1) {
	#print ();
	die "usage: my_tally <filename>\n";
}

open(IN, $ARGV[0]);

# read lines into a hash of arrays [key = count, value = digits and checksum]
while ($line = <IN>) {
	my ($temp, $checksum, $key) = split(' ', $line);
	$value = $temp.' '.$checksum;
	if (exists $lines{$key}) {
		push (@{$lines{$key}}, $value);
	} else {
		$lines{$key} = [$value];
	}
	#if ($DEBUG == 1) { print "grabbed $value $key\n"; }
}

close(IN);

# output the sorted hash of arrays to a file called "temp1"
open(TEMP1, ">temp1");
$i = 1;
foreach $key (reverse sort keys %lines) {
	foreach $value (@{$lines{$key}}) {
		print TEMP1 "$i $value $key\n";
		#if ($DEBUG == 1) { print "$i $value $key\n"; }
		$i++;
	}
}
close(TEMP1);

# output 2 columns of info [count and frequency] to a file called "temp2"
open(TEMP1, "temp1");
open(TEMP2, ">temp2");
my %lines = ();
while ($line = <TEMP1>) {
	#if ($DEBUG == 1) { print "processing $line"; }
	@temp = split(' ', $line);
	$key = $temp[$#temp];
	if (exists $lines{$key}) {
		$lines{$key} += 1;
	} else {
		$lines{$key} = 1;
	}
}
foreach $key (sort keys %lines) {
	print TEMP2 "$key " . $lines{$key} . "\n";
}
close(TEMP1);

# GNUPLOT temp1
open (GNUPLOT, "|gnuplot -persist") or die "no gnuplot found on system";
print GNUPLOT <<gnuplotCommands1;
plot "temp1" using 1:4 with lines
gnuplotCommands1
close GNUPLOT;

# GNUPLOT temp2
open (GNUPLOT, "|gnuplot -persist") or die "no gnuplot found on system";
print GNUPLOT <<gnuplotCommands2;
plot "temp2" using 1:2 with lines
gnuplotCommands2
close GNUPLOT;