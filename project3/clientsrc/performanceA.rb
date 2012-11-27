#!/usr/local/bin/ruby
$j = 0
while $j < 64 do
	output = File.open("#$j", "w") 
	$i = 0
	output.puts "start"
	while $i < 1000 do
		output.puts "newcar,0,#$j,1,1"
		output.puts "newroom,0,#$j,1,1"
		output.puts "newflight,0,#$j,1,1"
		$i += 1
	end
	output.puts "commit,0"
	output.close
	$j += 1
end