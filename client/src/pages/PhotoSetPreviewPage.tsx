import React, { useEffect, useRef } from "react"
import {
    Chart,
    ChartConfiguration,
    ChartData,
    ChartOptions,
} from "chart.js/auto"

interface WordFrequency {
    word: string
    count: number
}

interface FrequencyDistribution {
    frequency: number
    count: number
}

const WordDistributionChart: React.FC = () => {
    const chartRef = useRef<HTMLCanvasElement | null>(null)

    useEffect(() => {
        if (chartRef.current) {
            const ctx = chartRef.current.getContext("2d")
            if (ctx) {
                // Parse the word frequency data
                // TODO: import dummy data from tempData.txt in /assets
                const wordFrequencies: WordFrequency[] = `de,438 
la,372
le,311
il,237
les,204
et,196
à,167
commissaire,156
un,155
une,145
pas,145
vous,143
du,126
dans,120
est,120
je,120
ne,100
:,98
des,94
se,82`
                    .split("\n")
                    .map((line) => {
                        const [word, count] = line.split(",")
                        return { word, count: parseInt(count, 10) }
                    })

                // Group by frequency
                const frequencyDistribution = wordFrequencies.reduce<
                    Record<number, number>
                >((acc, { count }) => {
                    acc[count] = (acc[count] || 0) + 1
                    return acc
                }, {})

                // Convert to array and sort
                const distributionData: FrequencyDistribution[] =
                    Object.entries(frequencyDistribution)
                        .map(([frequency, count]) => ({
                            frequency: parseInt(frequency, 10),
                            count,
                        }))
                        .filter(({ frequency }) => frequency > 5) // Filter out low frequencies
                        .sort((a, b) => b.frequency - a.frequency)

                const data: ChartData<"bar"> = {
                    labels: distributionData.map((d) => d.frequency.toString()),
                    datasets: [
                        {
                            label: "Number of Words",
                            data: distributionData.map((d) => d.count),
                            backgroundColor: "rgba(75, 192, 192, 0.6)",
                            borderColor: "rgba(75, 192, 192, 1)",
                            borderWidth: 1,
                        },
                    ],
                }

                const options: ChartOptions<"bar"> = {
                    responsive: true,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: "Word Frequency",
                            },
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: "Number of Words",
                            },
                        },
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: "Distribution of Word Frequencies",
                        },
                        tooltip: {
                            callbacks: {
                                label: (context) => {
                                    const frequency = parseInt(
                                        context.label || "0",
                                        10
                                    )
                                    const count = context.parsed.y
                                    return `${count} word${
                                        count !== 1 ? "s" : ""
                                    } appeared ${frequency} time${
                                        frequency !== 1 ? "s" : ""
                                    }`
                                },
                            },
                        },
                    },
                }

                const config: ChartConfiguration<"bar"> = {
                    type: "bar",
                    data: data,
                    options: options,
                }

                new Chart(ctx, config)
            }
        }
    }, [])

    return (
        <div style={{ width: "80%", margin: "0 auto" }}>
            <canvas ref={chartRef}></canvas>
        </div>
    )
}

export default WordDistributionChart
