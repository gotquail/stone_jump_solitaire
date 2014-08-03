TODO:

Move stack restructure: Move interface, two subclasses: forward and backtrack moves. Calling doMove returns a new Move object. If it's a forward move then the returned value is the backtrack move that gets pushed onto the stack. If it's a backtrack then null gets returned -> don't do anything else.
	- I don't know if this is better... not very intuitive. I guess pushing on the reversal makes sense. More sense than just setting the flag. The problem is returning a reversal or null from doMove... that just doesn't make sense.
	- At least in the current form you get to call two different methods: doMove and reverseMove... and those make sense. So keeping the one Move object (instead of splitting it into forward and backward moves) is better.
Improve the search for available moves. Right now it searches all the available openings on the board. I should just keep track of them. As you make a move just search the newly-opened locations for new moves.
Try to get the board compression down to 32-bit from 64.
Heuristics? Determine some signs for when a board can't possibly lead to a solution.