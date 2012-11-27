#!/usr/local/bin/ruby

$i = 0
puts "start"
while $i < 1000 do 
	puts "newcar,0,#$i,1,1"
	puts "newcustomer,0,#$i"
	$i += 1
end
puts "commit,0"