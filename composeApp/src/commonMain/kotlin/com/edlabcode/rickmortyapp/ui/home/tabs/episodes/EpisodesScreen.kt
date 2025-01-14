package com.edlabcode.rickmortyapp.ui.home.tabs.episodes

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.edlabcode.rickmortyapp.domain.model.EpisodeModel
import com.edlabcode.rickmortyapp.domain.model.SeasonEpisode
import com.edlabcode.rickmortyapp.isDesktop
import com.edlabcode.rickmortyapp.ui.core.BackgroundPrimaryColor
import com.edlabcode.rickmortyapp.ui.core.BackgroundSecondaryColor
import com.edlabcode.rickmortyapp.ui.core.DefaultTextColor
import com.edlabcode.rickmortyapp.ui.core.components.PagingLoading
import com.edlabcode.rickmortyapp.ui.core.components.PagingType
import com.edlabcode.rickmortyapp.ui.core.components.PagingWrapper
import com.edlabcode.rickmortyapp.ui.core.components.VideoPlayer
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import rickmortyapp.composeapp.generated.resources.Res
import rickmortyapp.composeapp.generated.resources.placeHolder
import rickmortyapp.composeapp.generated.resources.portal
import rickmortyapp.composeapp.generated.resources.rickface
import rickmortyapp.composeapp.generated.resources.season1
import rickmortyapp.composeapp.generated.resources.season2
import rickmortyapp.composeapp.generated.resources.season3
import rickmortyapp.composeapp.generated.resources.season4
import rickmortyapp.composeapp.generated.resources.season5
import rickmortyapp.composeapp.generated.resources.season6
import rickmortyapp.composeapp.generated.resources.season7

@OptIn(KoinExperimentalAPI::class)
@Composable
fun EpisodesScreen() {
    val episodesViewmodel = koinViewModel<EpisodesViewModel>()
    val state by episodesViewmodel.state.collectAsState()
    val episodes = state.episodes.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundPrimaryColor)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        PagingWrapper(
            pagingType = PagingType.ROW,
            pagingItems = episodes,
            initialView = { PagingLoading() },
            itemView = { data -> EpisodeItemList(data) { url -> episodesViewmodel.onPlaySelected(url) } }
        )
        EpisodePlayer(state.playVideo) { episodesViewmodel.onPlaySelected("") }
    }
}

@Composable
private fun EpisodePlayer(playVideo: String, onCloseVideo: () -> Unit) {
    AnimatedContent(playVideo.isNotBlank()) { condition ->
        if (condition) {
            val height = if (isDesktop()) 600.dp else 250.dp
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().height(height).padding(16.dp)
                    .border(
                        3.dp, color = Color.Green,
                        shape = CardDefaults.elevatedShape
                    )
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    Box(
                        modifier = Modifier.padding(12.dp).background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        VideoPlayer(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            playVideo
                        )
                    }
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Image(painterResource(Res.drawable.portal),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(40.dp)
                                .clickable { onCloseVideo() }
                        )
                    }
                }
            }
        } else {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = BackgroundSecondaryColor)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.placeHolder),
                        contentDescription = null
                    )
                    Text(
                        "Aw, jeez, you gotta click the video, guys! I mean, it might be important or something!",
                        color = DefaultTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}

@Composable
private fun EpisodeItemList(data: EpisodeModel, onEpisodeSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onEpisodeSelected(data.videoURL) }
            .padding(horizontal = 8.dp)
    ) {
        Image(
            painter = painterResource(getResourceBySeason(data.season)),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            data.episode, color = DefaultTextColor, fontWeight = FontWeight.Bold,
            modifier = Modifier.height(24.dp)
        )
    }
}

private fun getResourceBySeason(season: SeasonEpisode): DrawableResource {
    return when (season) {
        SeasonEpisode.SEASON_1 -> Res.drawable.season1
        SeasonEpisode.SEASON_2 -> Res.drawable.season2
        SeasonEpisode.SEASON_3 -> Res.drawable.season3
        SeasonEpisode.SEASON_4 -> Res.drawable.season4
        SeasonEpisode.SEASON_5 -> Res.drawable.season5
        SeasonEpisode.SEASON_6 -> Res.drawable.season6
        SeasonEpisode.SEASON_7 -> Res.drawable.season7
        SeasonEpisode.UNKNOWN -> Res.drawable.rickface
    }
}

