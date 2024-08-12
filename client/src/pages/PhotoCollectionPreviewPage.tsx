import React, { useEffect, useRef, useState } from "react"
import {
    Chart,
    ChartConfiguration,
    ChartData,
    ChartOptions,
} from "chart.js/auto"
import ThreeStateNavToggle from "../components/PhotoCollectionNav"
import CollectionsViewer from "@/components/collectionsPreview/CollectionsViewer"
import {
    PhotoCollectionPageState,
    photoCollectionPageStates,
} from "@/util/pageStates"
import WordDistributionChart from "@/components/collectionsPreview/WordDistributionChart"
import WordList from "@/components/collectionsPreview/WordList"
import { Collection } from "@/interface/Collection.int"
import { dummyPhotoCollections } from "@/assets/dummyCollections"

// TODO: move this comment into the WordList component
// User can mark some words as 'misread,' 'non-existent,' 'bugs,' etc or 'not wanted' on this page
//

interface WordFrequency {
    word: string
    count: number
}

interface FrequencyDistribution {
    frequency: number
    count: number
}

const PhotoCollectionPreviewPage: React.FC = () => {
    // const chartRef = useRef<HTMLCanvasElement | null>(null)
    // const chartInstanceRef = useRef<Chart | null>(null)

    const [navState, setNavState] = useState<PhotoCollectionPageState>(
        photoCollectionPageStates.collections
    )

    const [collections, setCollections] = useState<Collection[]>(
        dummyPhotoCollections
    )

    // useEffect(() => {
    //     if (chartRef.current) {
    //         const ctx = chartRef.current.getContext("2d")
    //         if (ctx) {
    //             if (chartInstanceRef.current) {
    //                 chartInstanceRef.current.destroy()
    //             }
    //             // Parse the word frequency data
    //             // TODO: import dummy data from tempData.txt in /assets
    //             const wordFrequencies: WordFrequency[] = `de,438
    //                     la,372
    //                     le,311
    //                     il,237
    //                     les,204
    //                     et,196
    //                     à,167
    //                     commissaire,156
    //                     un,155
    //                     une,145
    //                     pas,145
    //                     vous,143
    //                     du,126
    //                     dans,120
    //                     est,120
    //                     je,120
    //                     ne,100
    //                     :,98
    //                     des,94
    //                     se,82`
    //                 .split("\n")
    //                 .map((line) => {
    //                     const [word, count] = line.split(",")
    //                     return { word, count: parseInt(count, 10) }
    //                 })

    //             // Group by frequency
    //             const frequencyDistribution = wordFrequencies.reduce<
    //                 Record<number, number>
    //             >((acc, { count }) => {
    //                 acc[count] = (acc[count] || 0) + 1
    //                 return acc
    //             }, {})

    //             // Convert to array and sort
    //             const distributionData: FrequencyDistribution[] =
    //                 Object.entries(frequencyDistribution)
    //                     .map(([frequency, count]) => ({
    //                         frequency: parseInt(frequency, 10),
    //                         count,
    //                     }))
    //                     .filter(({ frequency }) => frequency > 5) // Filter out low frequencies
    //                     .sort((a, b) => b.frequency - a.frequency)

    //             const data: ChartData<"bar"> = {
    //                 labels: distributionData.map((d) => d.frequency.toString()),
    //                 datasets: [
    //                     {
    //                         label: "Number of Words",
    //                         data: distributionData.map((d) => d.count),
    //                         backgroundColor: "rgba(75, 192, 192, 0.6)",
    //                         borderColor: "rgba(75, 192, 192, 1)",
    //                         borderWidth: 1,
    //                     },
    //                 ],
    //             }

    //             const options: ChartOptions<"bar"> = {
    //                 responsive: true,
    //                 scales: {
    //                     x: {
    //                         title: {
    //                             display: true,
    //                             text: "Word Frequency",
    //                         },
    //                     },
    //                     y: {
    //                         beginAtZero: true,
    //                         title: {
    //                             display: true,
    //                             text: "Number of Words",
    //                         },
    //                     },
    //                 },
    //                 plugins: {
    //                     title: {
    //                         display: true,
    //                         text: "Distribution of Word Frequencies",
    //                     },
    //                     tooltip: {
    //                         callbacks: {
    //                             label: (context) => {
    //                                 const frequency = parseInt(
    //                                     context.label || "0",
    //                                     10
    //                                 )
    //                                 const count = context.parsed.y
    //                                 return `${count} word${
    //                                     count !== 1 ? "s" : ""
    //                                 } appeared ${frequency} time${
    //                                     frequency !== 1 ? "s" : ""
    //                                 }`
    //                             },
    //                         },
    //                     },
    //                 },
    //             }

    //             const config: ChartConfiguration<"bar"> = {
    //                 type: "bar",
    //                 data: data,
    //                 options: options,
    //             }

    //             // Create and store the new chart instance
    //             chartInstanceRef.current = new Chart(ctx, config)
    //         }
    //     }
    // }, [])

    function renameCollection(idForRenaming: string, newLabel: string) {
        const current = JSON.parse(JSON.stringify(collections)) as Collection[]
        console.log(current, idForRenaming)
        const targetIndex = current.findIndex(
            (collection) => collection.id === idForRenaming
        )

        if (targetIndex !== -1) {
            const target = current[targetIndex]
            console.log(target, targetIndex, "176rm")
            target.label = newLabel // update in-place in the array

            setCollections(current)
        }
    }

    function renderComponent() {
        return navState === photoCollectionPageStates.collections ? (
            <CollectionsViewer
                collections={collections}
                onRenameCollection={renameCollection}
            />
        ) : navState === photoCollectionPageStates.chart ? (
            <WordDistributionChart />
        ) : (
            <WordList />
        )
    }

    return (
        <div style={styles.container}>
            <div style={{ width: "80%", margin: "0 auto" }}>
                <div>
                    <h1 style={styles.title}>Photo Set Preview</h1>
                </div>
                <ThreeStateNavToggle onStateChange={setNavState} />
                {renderComponent()}
            </div>
        </div>
    )
}

const styles = {
    container: {
        margin: "40px 0 0 0",
        border: "4px solid red",
    },
    title: {
        fontSize: "2.5em",
        marginBottom: "20px",
    },
}

export default PhotoCollectionPreviewPage
