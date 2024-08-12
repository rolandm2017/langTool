export type PhotoCollectionPageState = "collections" | "chart" | "list"

const photoCollectionPageStates = {
    collections: "collections",
    chart: "chart",
    list: "list",
} as const satisfies Record<PhotoCollectionPageState, PhotoCollectionPageState> // ts is a bad tool.

type Drink = "water" | "wine" | "juice"
type Animal = "hats" | "bats" | "cats"

const v = {
    water: "hats",
    wine: "bats",
    juice: "cats",
} as const satisfies Record<Drink, Animal>

const aaa = {
    hats: "water",
    bats: "wine",
    cats: "juice",
} as const satisfies Record<Animal, Drink>

export { photoCollectionPageStates }
