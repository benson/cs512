#!/usr/local/bin/ruby

$i = 0
puts "start"
while $i < 1000 do 
	puts "newcar,0,#$i,1,1"
	puts "newroom,0,#$i,1,1"
	puts "newflight,0,#$i,1,1"
	puts "newcustomer,0,#$i"
	$i += 1
end
puts "commit,0"
puts "start"
puts "starttiming"
$i= 0
while $i < 1000 do 
	puts "itinerary,1,#$i,#$i,#$i,true,true"
	$i += 1
end
puts "stoptiming"