#!/usr/local/bin/ruby


$i = 0
puts "start"
# get read locks on everything
# while $i < 1000 do 
# 	puts "querycar,1,#$i"
# 	$i += 1
# end
puts "commit,1"
puts "start"
puts "starttiming"
$i= 0
while $i < 1000 do 
	puts "reservecar,2,#$i,#$i"
	$i += 1
end
puts "stoptiming"