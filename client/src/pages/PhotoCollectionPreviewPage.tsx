import React, { useEffect, useRef, useState } from "react"
import { useSearchParams } from "react-router-dom"

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
import { Collection } from "@/interface/interfaces.int"
import { dummyPhotoCollections } from "@/assets/dummyCollections"
import useGetCollections from "@/api/useGetCollections.api"
import useGetNextCollectionId from "@/api/useGetNextCollectionId.api"
import { useUpdateCollectionLabel } from "@/api/useUpdateCollectionLabel.api"

// TODO: move this comment into the WordList component
// User can mark some words as 'misread,' 'non-existent,' 'bugs,' etc or 'not wanted' on this page
//

const PhotoCollectionPreviewPage: React.FC = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const [navState, setNavState] = useState<PhotoCollectionPageState>(
        photoCollectionPageStates.collections
    )

    useEffect(() => {
        const option = searchParams.get("option")
        const isValidPhotoCollectionPageState = (
            state: unknown
        ): state is PhotoCollectionPageState =>
            ["collections", "chart", "list"].includes(state as string)
        if (isValidPhotoCollectionPageState(option)) {
            const typedVariable: PhotoCollectionPageState = option
            switch (typedVariable) {
                case photoCollectionPageStates.collections:
                    setNavState(typedVariable)
                    break
                case photoCollectionPageStates.chart:
                    setNavState(typedVariable)
                    break
                case photoCollectionPageStates.list:
                    setNavState(typedVariable)
                    break
                default:
                    console.error("Error: Invalid page type: " + option)
                    setNavState(photoCollectionPageStates.collections)
            }
        }
    }, [searchParams])

    const [pageError, setPageError] = useState("")

    const [collections, setCollections] = useState<Collection[]>(
        dummyPhotoCollections
    )

    const {
        collections: loadedCollections,
        loading,
        error,
    } = useGetCollections() // todo

    const { updateCollectionLabel, error: updateError } =
        useUpdateCollectionLabel()

    useEffect(() => {
        if (loading) {
            return
        }
        if (loadedCollections) {
            setCollections(loadedCollections)
        }
    }, [loading])

    function renameCollection(idForRenaming: string, newLabel: string) {
        // tell the backend
        updateCollectionLabel({ idForRenaming, newLabel }).then((success) => {
            if (!success) {
                setPageError(
                    updateError
                        ? updateError
                        : "An error occurred during renaming. Try again or report the problem."
                )
            }
        })

        // tell the client
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

    function handleChangeNavStateAndUrlParam(
        newState: PhotoCollectionPageState
    ) {
        setNavState(newState)
        setSearchParams({ option: newState })
    }

    return (
        <div style={styles.container}>
            <div style={{ width: "80%", margin: "0 auto" }}>
                <div>
                    <h1 style={styles.title}>Photo Set Preview</h1>
                </div>
                <ThreeStateNavToggle
                    currentState={navState}
                    onStateChange={handleChangeNavStateAndUrlParam}
                />
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
