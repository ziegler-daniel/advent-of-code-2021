package day23;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Day23 {

    public static void main(String[] args) throws IOException {
        // int[] sampleRooms = {1, 2, 4, 3, 3, 2, 1, 4};
        int[] challengeRooms = {4, 4, 1, 3, 1, 2, 2, 3};

        part1(challengeRooms);
        part2(challengeRooms);
    }

    private static void part1(int[] rooms) {
        System.out.printf("Solution part 1: %d%n", new Solver(2).solve(new State(rooms)).cost);
    }

    private static void part2(int[] rooms) {
        int[] extendedRooms = {
                rooms[0], 4, 4, rooms[1],
                rooms[2], 2, 3, rooms[3],
                rooms[4], 1, 2, rooms[5],
                rooms[6], 3, 1, rooms[7]
        };

        System.out.printf("Solution part 2: %d%n", new Solver(4).solve(new State(extendedRooms)).cost);
    }

    @AllArgsConstructor
    static class Solver {
        private static final int[] ROOM_ENTRIES = {2, 4, 6, 8};
        private static final Set<Integer> ROOM_ENTRIES_SET = Set.of(2, 4, 6, 8);
        private static final long[] COST_MAP = {0, 1, 10, 100, 1000};

        final int roomSize;
        Map<State, State> dp;

        public Solver(int roomSize) {
            this.roomSize = roomSize;
            dp = new HashMap<>();
        }

        private State solve(State state) {
            if (isDone(state)) {
                return state;
            }

            if (dp.containsKey(state)) {
                return dp.get(state);
            }

            State bestResult = null;

            // Move one amphipod from the hallway into its target room
            for (int i = 0; i < 11; ++i) {
                if (state.hallway[i] > 0 && canMoveToDestinationRoom(i, state)) {
                    State result = moveToRoom(i, state);
                    if (result != null && (bestResult == null || result.cost < bestResult.cost)) {
                        bestResult = result;
                    }
                }
            }

            // Move one amphipods out of its room on the hallway
            for (int i = 0; i < state.rooms.length; ++i) {
                if (state.rooms[i] > 0 && canMoveOut(state, i) && state.hallway[ROOM_ENTRIES[i / roomSize]] == 0) {
                    // Go on the hallway to the left
                    for (int p = ROOM_ENTRIES[i / roomSize] - 1; p >= 0; --p) {
                        if (state.hallway[p] > 0) {
                            break;
                        }

                        if (!ROOM_ENTRIES_SET.contains(p)) {
                            State result = moveToHallway(i, p, state);
                            if (result != null && (bestResult == null || result.cost < bestResult.cost)) {
                                bestResult = result;
                            }
                        }
                    }

                    // Go on the hallway to the right
                    for (int p = ROOM_ENTRIES[i / roomSize] + 1; p < 11; ++p) {
                        if (state.hallway[p] > 0) {
                            break;
                        }

                        if (!ROOM_ENTRIES_SET.contains(p)) {
                            State result = moveToHallway(i, p, state);
                            if (result != null && (bestResult == null || result.cost < bestResult.cost)) {
                                bestResult = result;
                            }
                        }
                    }
                }
            }

            dp.put(state, bestResult);
            return bestResult;
        }

        private boolean isDone(State state) {
            for (int i = 0; i < state.rooms.length; ++i) {
                if (state.rooms[i] != (i / roomSize) + 1) {
                    return false;
                }
            }

            return true;
        }

        private State moveToHallway(int roomIndex, int hallwayIndex, State state) {
            int amphipod = state.rooms[roomIndex];
            int steps = roomSize - (roomIndex % roomSize) + Math.abs(ROOM_ENTRIES[roomIndex / roomSize] - hallwayIndex);

            State copy = state.copy();
            copy.rooms[roomIndex] = 0;
            copy.hallway[hallwayIndex] = amphipod;
            copy.cost += steps * COST_MAP[amphipod];

            return solve(copy);
        }

        private State moveToRoom(int hallwayIndex, State state) {
            int destinationRoom = state.hallway[hallwayIndex] - 1;
            int amphipod = state.hallway[hallwayIndex];

            State copy = state.copy();
            copy.hallway[hallwayIndex] = 0;

            int stepsOnHallway = Math.abs(hallwayIndex - ROOM_ENTRIES[destinationRoom]);

            for (int i = 0; i < roomSize; ++i) {
                if (state.rooms[destinationRoom * roomSize + i] == 0) {
                    copy.rooms[destinationRoom * roomSize + i] = amphipod;
                    copy.cost += (roomSize - i + stepsOnHallway) * COST_MAP[amphipod];
                    return solve(copy);
                }
            }

            System.err.println("error move to room");
            return null;
        }

        private boolean canMoveToDestinationRoom(int hallwayIndex, State state) {
            int destinationRoom = state.hallway[hallwayIndex] - 1;
            int destinationRoomEntry = ROOM_ENTRIES[destinationRoom];

            // The path on the hallway to the room must be free
            if (destinationRoomEntry < hallwayIndex) {
                for (int i = destinationRoomEntry; i < hallwayIndex; ++i) {
                    if (state.hallway[i] > 0) {
                        return false;
                    }
                }
            } else {
                for (int i = hallwayIndex + 1; i <= destinationRoomEntry; ++i) {
                    if (state.hallway[i] > 0) {
                        return false;
                    }
                }
            }

            // There must be space in the target room and there should be not amphipods that needs to move out
            for (int i = 0; i < roomSize; ++i) {
                if (state.rooms[destinationRoom * roomSize + i] == 0) {
                    return true;
                }
                if (state.rooms[destinationRoom * roomSize + i] != destinationRoom + 1) {
                    return false;
                }
            }

            return false;
        }

        private boolean canMoveOut(State state, int roomIndex) {
            int room = roomIndex / roomSize;
            int positionInRoom = roomIndex % roomSize;

            if (allCorrectInRoomUntilPos(state, room, positionInRoom)) {
                return false;
            }

            // The amphipods needs to move out -> Check if the space above is free
            for (int i = positionInRoom + 1; i < roomSize; ++i) {
                if (state.rooms[room * roomSize + i] > 0) {
                    return false;
                }
            }

            return true;
        }

        private boolean allCorrectInRoomUntilPos(State state, int room, int positionInRoom) {
            for (int i = 0; i <= positionInRoom; ++i) {
                if (state.rooms[room * roomSize + i] != room + 1) {
                    return false;
                }
            }
            return true;
        }
    }

    @AllArgsConstructor
    @Data
    static class State {
        long cost;
        int[] hallway;
        int[] rooms;


        public State(int[] rooms) {
            this.rooms = rooms;
            this.hallway = new int[11];
            this.cost = 0;
        }

        State copy() {
            return new State(cost, Arrays.copyOf(hallway, hallway.length), Arrays.copyOf(rooms, rooms.length));
        }

    }
}
