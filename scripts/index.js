// Activating strict mode
"use strict";

// Creating constants for DOM interaction
const INPUT_DISK_COUNT = document.getElementById('disk-count');
const BUTTON_CALCULATE_MOVES = document.getElementById('calculate-moves');
const CANVAS_OUTPUT = document.getElementById('output');
const CANVAS_OUTPUT_CONTEXT = CANVAS_OUTPUT.getContext('2d');
const BUTTON_PREVIOUS_MOVE = document.getElementById('previous-move');
const BUTTON_NEXT_MOVE = document.getElementById('next-move');

// Global variables
var moves = [];
var scalingRatio = 1;
var diskCount = 0;
var visibleMove = 0;

// An iterative algorithm to solve the game
function calculateMoves()
{
    // Creating the rod start configuration
    let data = [
                   [],
                   [],
                   []
               ];
    for (var i = 0; i < diskCount; i++)
    {
        data[0].push(i);
    }
    // Resetting the move history
    moves = [JSON.parse(JSON.stringify(data))];
    // Setting the movement direction for disk 0 following this scheme: [last, current, next]
    let disk0Positions = [];
    if (diskCount % 2 === 0)
    {
        // Moving disk 0 through the rods from A to C when having an even number of disks
        disk0Positions = [2, 0, 1];
    }
    else
    {
        // Moving disk 0 through the rods from C to A when having an odd number of disks
        disk0Positions = [1, 0, 2];
    }
    // Moving disks until all of them are on rod C and in the right order
    while (data[2].length < diskCount)
    {
        // Moving disk 0 to the next rod
        data[disk0Positions[2]].unshift(0);
        data[disk0Positions[1]].shift();
        // Storing the current move
        moves.push(JSON.parse(JSON.stringify(data)));
        if (data[disk0Positions[0]][0] < data[disk0Positions[1]][0] || (data[disk0Positions[1]][0] === undefined && data[2].length < diskCount))
        {
            // Moving another disk from second last to last position of disk 0 if possible
            data[disk0Positions[1]].unshift(data[disk0Positions[0]][0]);
            data[disk0Positions[0]].shift();
            // Storing the current move
            moves.push(JSON.parse(JSON.stringify(data)));
        }
        else if (data[disk0Positions[1]][0] < data[disk0Positions[0]][0] || (data[disk0Positions[0]][0] === undefined && data[2].length < diskCount))
        {
            // Moving another disk from last to second last position of disk 0 if possible
            data[disk0Positions[0]].unshift(data[disk0Positions[1]][0]);
            data[disk0Positions[1]].shift();
            // Storing the current move
            moves.push(JSON.parse(JSON.stringify(data)));
        }
        // Setting new positions of disk 0 for the next iteration
        disk0Positions.push(disk0Positions[0]);
        disk0Positions.shift();
    }
}

// A small helper function for resizing the canvas
function resizeCanvas()
{
    // Sizing the canvas to the window width
    CANVAS_OUTPUT.width = document.documentElement.clientWidth;
    CANVAS_OUTPUT.height = document.documentElement.clientWidth / (300 / 120);
    // Updating the scaling ratio to fit the internal resolution of 300 x 110 to the real canvas size
    scalingRatio = CANVAS_OUTPUT.width / 300;
    // Displaying a small informational text on the canvas
    CANVAS_OUTPUT_CONTEXT.font = (5 * scalingRatio) + 'px sans-serif';
    CANVAS_OUTPUT_CONTEXT.textAlign = 'center';
    CANVAS_OUTPUT_CONTEXT.textBaseline = 'center';
    CANVAS_OUTPUT_CONTEXT.fillText('Click "Calculate moves" to see the visualization', 150 * scalingRatio, 55 * scalingRatio);
    // Repainting the canvas when needed
    if (moves.length > 0)
    {
        visualizeMove(visibleMove);
    }
}

// An algorithm to visualize the game states
function visualizeMove(gameState = 0)
{
    // Calculating the disk scaling ratio (width difference between different disk)
    let diskScalingRatio = 100 / diskCount;
    // Clearing the canvas
    CANVAS_OUTPUT_CONTEXT.clearRect(0, 0, CANVAS_OUTPUT.width, CANVAS_OUTPUT.height);
    // Drawing game state label
    CANVAS_OUTPUT_CONTEXT.font = (10 * scalingRatio) + 'px sans-serif';
    CANVAS_OUTPUT_CONTEXT.fillText(gameState + '/' + (moves.length - 1), 150 * scalingRatio, 9 * scalingRatio);
    for (var i = 0; i < 3; i++)
    {
        // Drawing a rod
        CANVAS_OUTPUT_CONTEXT.fillStyle = "#0000ff";
        CANVAS_OUTPUT_CONTEXT.fillRect((47.5 + (100 * i)) * scalingRatio, 10 * scalingRatio, 5 * scalingRatio, 100 * scalingRatio);
        // Drawing the disks on the rod
        CANVAS_OUTPUT_CONTEXT.fillStyle = "#000000";
        for (var j = 0; j < moves[gameState][i].length; j++)
        {
            let diskWidth = diskScalingRatio * (moves[gameState][i][j] + 1);
            CANVAS_OUTPUT_CONTEXT.fillRect((((100 - diskWidth) / 2) + (100 * i)) * scalingRatio, ((111 - (10 * moves[gameState][i].length)) + (10 * j)) * scalingRatio, diskWidth * scalingRatio, 9 * scalingRatio);
        }
    }
    // Drawing rod labels
    CANVAS_OUTPUT_CONTEXT.fillText('A', 50 * scalingRatio, 118 * scalingRatio);
    CANVAS_OUTPUT_CONTEXT.fillText('B', 150 * scalingRatio, 118 * scalingRatio);
    CANVAS_OUTPUT_CONTEXT.fillText('C', 250 * scalingRatio, 118 * scalingRatio);
}

// Handling button clicks
BUTTON_CALCULATE_MOVES.addEventListener('click', function ()
{
    diskCount = INPUT_DISK_COUNT.value;
    if (diskCount < 0)
    {
        diskCount = 0;
    }
    else if (diskCount > 10)
    {
        diskCount = 10;
    }
    calculateMoves();
    visibleMove = 0;
    visualizeMove(visibleMove);
});
BUTTON_PREVIOUS_MOVE.addEventListener('click', function ()
{
    if (visibleMove > 0)
    {
        visibleMove--;
        visualizeMove(visibleMove);
    }
});
BUTTON_NEXT_MOVE.addEventListener('click', function ()
{
    if (visibleMove < moves.length - 1)
    {
        visibleMove++;
        visualizeMove(visibleMove);
    }
});
// Fitting the canvas to the window size
window.addEventListener('resize', resizeCanvas);
window.addEventListener('rotate', resizeCanvas);
resizeCanvas();
resizeCanvas();

