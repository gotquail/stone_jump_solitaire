there are 37 points on the board (49 if you want it to be square), so the board state can be represeneted by a single 64-bit number, since all board slots are booleans.

I can hash board states as I see them -> don't go down the same route multiple times.

Avoid redundancy by checking if the board state already exists in a rotated or mirrored (or both) equivalent. 4 rotation states, 2 mirror states -> 8 checks total.

