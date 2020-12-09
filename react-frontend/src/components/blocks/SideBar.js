import React, { useEffect, useState } from 'react'
import { getFileSize } from '../../helpers/file'
import '../../styles/blocks/sidebar.scss'

/*
    @props {Object} storageData
*/
function SideBar({ storageData }) {
    // Array of objects { class, sizePercentage }
    const [ storageBarDisplayData, setStorageBarDisplayData ] = useState([])
    
    useEffect(() => {
        const {
            currentUsedSpace,
            storageInImages,
            storageInVideos,
            storageInAudio,
            storageInDocs,
            storageInOthers } = storageData

        let barDisplayData = [
            { class: 'images', sizePercentage: getPercentageFromBytes(storageInImages, currentUsedSpace) },
            { class: 'videos', sizePercentage: getPercentageFromBytes(storageInVideos, currentUsedSpace) },
            { class: 'audio', sizePercentage: getPercentageFromBytes(storageInAudio, currentUsedSpace) },
            { class: 'docs', sizePercentage: getPercentageFromBytes(storageInDocs, currentUsedSpace) },
            { class: 'others', sizePercentage: getPercentageFromBytes(storageInOthers, currentUsedSpace) },
        ]

        barDisplayData = barDisplayData
            .sort((a, b) => a.sizePercentage < b.sizePercentage) // Sort values

        let accumulatedSum = 0;

        // Correct the sizePercentage to prevent bar overlaping
        barDisplayData = barDisplayData.map(fileType =>{
            accumulatedSum += fileType.sizePercentage

            return {
                ...fileType,
                sizePercentage: accumulatedSum
            }
        })
        .reverse() // Reverse so the smallest bar is the last one being rendered

        setStorageBarDisplayData(barDisplayData)
    }, [storageData])

    function getPercentageFromBytes(valInBytes, maxValInBytes) {
        return (valInBytes * 100) / maxValInBytes
    }

    return (
        <section className="sidebar">
            <section className="cloud-space-container">
                <div className="space—usage-display">
                    <div className="labels">
                        <h1>{ getFileSize(storageData.currentUsedSpace) }</h1>
                        <p>{ getFileSize(storageData.currentAvailableSpace) } available</p>
                    </div>
                    <div className="bar space-bar">
                        <div className={'space-filler curr-space'}
                            style={{width: getPercentageFromBytes(
                                storageData.currentUsedSpace,
                                storageData.maxAvailableSpace
                            ) + '%'}}></div>
                    </div>
                    <div className="bar file-type-space-bar">
                        { storageBarDisplayData.length > 0 &&
                            storageBarDisplayData.map(fileType => 
                                <div key={fileType.class}
                                    className={'space-filler ' + fileType.class}
                                    style={{width: fileType.sizePercentage + '%'}}></div>
                            ) }
                    </div>
                </div>
                <div className="file-type-desc">
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Image</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageInImages) }</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Videos</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageInVideos) }</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Audio</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageInAudio) }</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Docs</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageInDocs) }</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Others</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageInOthers) }</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Available</label>
                        <label className="space-used light-text">{ getFileSize(storageData.storageIn) }</label>
                    </div>
                </div>
            </section>
            <section className="file-info-container">
                <header>
                    <div className="id-row">
                        <div className="file-type-icon"></div>
                        <h2 className="file-name">File name</h2>
                        <label className="file-size light-text">8KB</label>
                    </div>
                    <p className="file-last-mod">Last modified Sep 3, 2019</p>
                </header>
                <div className="action-buttons">
                    <button className="action-btn">
                        <img src="./assets/icons/download_icon.svg" alt="" /> <label className="dark-text">Download</label>
                    </button>
                    <button className="action-btn">
                        <img src="./assets/icons/rename_icon.svg" alt="" /> <label className="dark-text">Rename</label>
                    </button>
                    <button className="action-btn">
                        <img src="./assets/icons/move_icon.svg" alt="" /> <label className="dark-text">Move</label>
                    </button>
                    <div className="separator"></div>
                    <button className="action-btn delete-btn">
                        <img src="./assets/icons/delete_icon.svg" alt="" /> <label className="dark-text">Delete</label>
                    </button>
                </div>
            </section>
        </section>
    )
}

export default SideBar