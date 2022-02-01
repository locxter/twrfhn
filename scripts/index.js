'use strict';

// DOM references
const DISK_COUNT = document.getElementById('visualization-disk-count');
const CALCULATE_GAME_STATES = document.getElementById('visualization-calculate-game-states');
const CANVAS = document.getElementById('visualization-canvas');
const CANVAS_CONTEXT = CANVAS.getContext('2d');
const PREVIOUS_GAME_STATE = document.getElementById('visualization-previous-game-state');
const NEXT_GAME_STATE = document.getElementById('visualization-next-game-state');

// Global variables
let gameStates = [];
let scalingRatio = 1;
let diskCount = 0;
let visibleGameState = 0;
let textColor = getComputedStyle(document.documentElement).getPropertyValue('--text-color');
let accentColor = getComputedStyle(document.documentElement).getPropertyValue('--accent-color');

// A small helper function for resizing the canvas
function modifyCanvas()
{
    // Sizing the canvas to the window width
    CANVAS.width = CANVAS.parentElement.clientWidth;
    CANVAS.height = CANVAS.parentElement.clientWidth * (1080 / 1920);
    // Updating the scaling ratio to fit the internal resolution of 1920x1080 to the real canvas size
    scalingRatio = CANVAS.width / 1920;
    // Displaying a small informational text on the canvas
    CANVAS_CONTEXT.fillStyle = textColor;
    CANVAS_CONTEXT.font = (54 * scalingRatio) + 'px sans-serif';
    CANVAS_CONTEXT.textAlign = 'center';
    CANVAS_CONTEXT.textBaseline = 'middle';
    CANVAS_CONTEXT.fillText('Click Calculate game states to see the visualization.', 960 * scalingRatio, 540 * scalingRatio);
    // Repainting the canvas when needed
    if (gameStates.length > 0)
    {
        visualizeGameState(visibleGameState);
    }
}

// An iterative algorithm to solve the game
function calculateGameStates()
{
    // Creating the rod start configuration
    let rods = [
                   [],
                   [],
                   []
               ];
    for (let i = 0; i < diskCount; i++)
    {
        rods[0].push(i);
    }
    // Resetting the gamestate history
    gameStates = [JSON.parse(JSON.stringify(rods))];
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
    while (rods[2].length < diskCount)
    {
        // Moving disk 0 to the next rod
        rods[disk0Positions[2]].unshift(0);
        rods[disk0Positions[1]].shift();
        // Storing the current move
        gameStates.push(JSON.parse(JSON.stringify(rods)));
        if (rods[disk0Positions[0]][0] < rods[disk0Positions[1]][0] || (rods[disk0Positions[1]][0] === undefined && rods[2].length < diskCount))
        {
            // Moving another disk from second last to last position of disk 0 if possible
            rods[disk0Positions[1]].unshift(rods[disk0Positions[0]][0]);
            rods[disk0Positions[0]].shift();
            // Storing the current move
            gameStates.push(JSON.parse(JSON.stringify(rods)));
        }
        else if (rods[disk0Positions[1]][0] < rods[disk0Positions[0]][0] || (rods[disk0Positions[0]][0] === undefined && rods[2].length < diskCount))
        {
            // Moving another disk from last to second last position of disk 0 if possible
            rods[disk0Positions[0]].unshift(rods[disk0Positions[1]][0]);
            rods[disk0Positions[1]].shift();
            // Storing the current move
            gameStates.push(JSON.parse(JSON.stringify(rods)));
        }
        // Setting new positions of disk 0 for the next iteration
        disk0Positions.push(disk0Positions[0]);
        disk0Positions.shift();
    }
}

// An algorithm to visualize the game states
function visualizeGameState(gameState = 0)
{
    // Calculating the disk scaling ratio (width difference between different disk)
    let diskScalingRatio = 544 / diskCount;
    // Clearing the canvas
    CANVAS_CONTEXT.clearRect(0, 0, CANVAS.width, CANVAS.height);
    // Drawing the game state label
    CANVAS_CONTEXT.fillStyle = textColor;
    CANVAS_CONTEXT.font = (54 * scalingRatio) + 'px sans-serif';
    CANVAS_CONTEXT.fillText(gameState + '/' + (gameStates.length - 1), 960 * scalingRatio, 54 * scalingRatio);
    for (let i = 0; i < 3; i++)
    {
        // Drawing a rod
        CANVAS_CONTEXT.fillStyle = textColor;
        CANVAS_CONTEXT.fillRect((296 + (640 * i)) * scalingRatio, 108 * scalingRatio, 48 * scalingRatio, 864 * scalingRatio);
        // Drawing the disks on the rod
        CANVAS_CONTEXT.fillStyle = accentColor;
        for (let j = 0; j < gameStates[gameState][i].length; j++)
        {
            let diskWidth = diskScalingRatio * (gameStates[gameState][i][j] + 1);
            CANVAS_CONTEXT.fillRect(((320 - (diskWidth / 2)) + (640 * i)) * scalingRatio, 
            ((972 - (86.4 * gameStates[gameState][i].length)) + (86.4 * j)) * scalingRatio, diskWidth * scalingRatio, 80 * scalingRatio);
        }
    }
    // Drawing rod labels
    CANVAS_CONTEXT.fillStyle = textColor;
    CANVAS_CONTEXT.fillText('A', 320 * scalingRatio, 1026 * scalingRatio);
    CANVAS_CONTEXT.fillText('B', 960 * scalingRatio, 1026 * scalingRatio);
    CANVAS_CONTEXT.fillText('C', 1600 * scalingRatio, 1026 * scalingRatio);
}

// Fitting the canvas to the window size
addEventListener('load', function()
{
    modifyCanvas();
    modifyCanvas();
});
addEventListener('resize', function()
{
    modifyCanvas();
    modifyCanvas();
});
addEventListener('rotate', function()
{
    modifyCanvas();
    modifyCanvas();
});

// Reacting to color preference change
matchMedia('(prefers-color-scheme: dark)').addListener(function(mediaQuery)
{
    textColor = getComputedStyle(document.documentElement).getPropertyValue('--text-color');
    accentColor = getComputedStyle(document.documentElement).getPropertyValue('--accent-color');
    modifyCanvas();
});

// Handling button clicks
CALCULATE_GAME_STATES.addEventListener('click', function ()
{
    diskCount = DISK_COUNT.value;
    if (diskCount < 1)
    {
        diskCount = 1;
    }
    else if (diskCount > 10)
    {
        diskCount = 10;
    }
    calculateGameStates();
    visibleGameState = 0;
    visualizeGameState(visibleGameState);
});

PREVIOUS_GAME_STATE.addEventListener('click', function ()
{
    if (visibleGameState > 0)
    {
        visibleGameState--;
        visualizeGameState(visibleGameState);
    }
});

NEXT_GAME_STATE.addEventListener('click', function ()
{
    if (visibleGameState < gameStates.length - 1)
    {
        visibleGameState++;
        visualizeGameState(visibleGameState);
    }
});
