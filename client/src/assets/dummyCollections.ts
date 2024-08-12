import { Collection } from "@/interface/Collection.int"

export const dummyPhotoCollections: Collection[] = [
    {
        id: "nature-1",
        label: "Nature Wonders",
        photos: [
            {
                id: "nat-001",
                fileName: "mountain_sunrise.jpg",
                words: [
                    "mountain",
                    "sunrise",
                    "landscape",
                    "serene",
                    "majestic",
                ],
            },
            {
                id: "nat-002",
                fileName: "tropical_beach.jpg",
                words: ["beach", "palm trees", "ocean", "sand", "paradise"],
            },
            {
                id: "nat-003",
                fileName: "autumn_forest.jpg",
                words: ["forest", "autumn", "foliage", "colorful", "trees"],
            },
        ],
    },
    {
        id: "urban-1",
        label: "City Life",
        photos: [
            {
                id: "urb-001",
                fileName: "skyscrapers.jpg",
                words: [
                    "skyscrapers",
                    "city",
                    "architecture",
                    "modern",
                    "tall",
                ],
            },
            {
                id: "urb-002",
                fileName: "street_cafe.jpg",
                words: ["cafe", "street", "people", "coffee", "socializing"],
            },
            {
                id: "urb-003",
                fileName: "subway_station.jpg",
                words: [
                    "subway",
                    "transport",
                    "commute",
                    "underground",
                    "urban",
                ],
            },
            {
                id: "urb-004",
                fileName: "night_skyline.jpg",
                words: ["skyline", "night", "lights", "cityscape", "vibrant"],
            },
        ],
    },
    {
        id: "food-1",
        label: "Culinary Delights",
        photos: [
            {
                id: "food-001",
                fileName: "sushi_platter.jpg",
                words: ["sushi", "Japanese", "seafood", "gourmet", "fresh"],
            },
            {
                id: "food-002",
                fileName: "pizza_oven.jpg",
                words: ["pizza", "Italian", "oven", "cheese", "tomato"],
            },
            {
                id: "food-003",
                fileName: "fruit_bowl.jpg",
                words: ["fruit", "healthy", "colorful", "fresh", "assorted"],
            },
        ],
    },
    {
        id: "tech-1",
        label: "Technology Today",
        photos: [
            {
                id: "tech-001",
                fileName: "smartphone.jpg",
                words: [
                    "smartphone",
                    "mobile",
                    "technology",
                    "apps",
                    "communication",
                ],
            },
            {
                id: "tech-002",
                fileName: "laptop_desk.jpg",
                words: [
                    "laptop",
                    "workspace",
                    "productivity",
                    "modern",
                    "gadget",
                ],
            },
            {
                id: "tech-003",
                fileName: "virtual_reality.jpg",
                words: ["VR", "headset", "virtual", "gaming", "immersive"],
            },
            {
                id: "tech-004",
                fileName: "robot_arm.jpg",
                words: [
                    "robot",
                    "automation",
                    "industry",
                    "future",
                    "mechanical",
                ],
            },
        ],
    },
]
