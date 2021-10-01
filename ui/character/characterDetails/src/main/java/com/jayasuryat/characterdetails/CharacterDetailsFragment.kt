package com.jayasuryat.characterdetails

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.jayasuryat.base.arch.BaseAbsFragment
import com.jayasuryat.base.shrinkOnClick
import com.jayasuryat.characterdetails.UiUtils.loadImage
import com.jayasuryat.characterdetails.databinding.FragmentCharacterDetailsBinding
import com.jayasuryat.data.models.domain.Character
import com.jayasuryat.data.models.domain.Character.Gender.*
import com.jayasuryat.data.models.domain.Character.Species.Alien
import com.jayasuryat.data.models.domain.Character.Species.Human
import com.jayasuryat.data.models.domain.Character.Status.Alive
import com.jayasuryat.data.models.domain.Character.Status.Dead
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


@AndroidEntryPoint
class CharacterDetailsFragment : BaseAbsFragment<CharacterDetailsViewModel,
        FragmentCharacterDetailsBinding>(FragmentCharacterDetailsBinding::inflate) {

    override val viewModel: CharacterDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun setupViews(): FragmentCharacterDetailsBinding.() -> Unit = {

        animateViews()

        cvBack.shrinkOnClick(::navigateBack)

        cvLocation.shrinkOnClick { }
    }

    override fun setupObservers(): CharacterDetailsViewModel.() -> Unit = {

        obsCharacter.observe(viewLifecycleOwner, ::loadUi)
    }

    private fun loadUi(character: Character?) {

        if (character == null) return

        binding.apply {

            ivCharacter.loadImage(character.image)
            tvName.text = character.name

            @IdRes val speciesImageId = when (character.species) {
                Alien -> R.drawable.icon_alien
                Human -> R.drawable.icon_user
            }

            @IdRes val genderImageId = when (character.gender) {
                Female -> R.drawable.icon_gender_female
                Male -> R.drawable.icon_gender_male
                Unknown -> R.drawable.icon_gender_neutral
            }

            @IdRes val statusColorId = when (character.status) {
                Alive -> R.color.green
                Dead -> R.color.red
                Character.Status.Unknown -> R.color.grey
            }

            ivSpecies.setImageResource(speciesImageId)
            ivGender.setImageResource(genderImageId)
            cvStatus.setCardBackgroundColor(ContextCompat.getColor(root.context, statusColorId))

            tvSpecies.text = character.species.name
            tvGender.text = character.gender.name
            tvStatus.text = character.status.name
            tvLocationValue.text = character.location.name
        }
    }

    private fun navigateBack() = EventBus.getDefault().post(NavigateBack)

    private fun animateViews() {

        val animDuration: Long = 300
        val animInterpolator = DecelerateInterpolator()

        TranslateAnimation(-100f, 0f, 0f, 0f)
            .apply {
                duration = animDuration
                interpolator = animInterpolator
            }.run { binding.cvBack.startAnimation(this) }

        TranslateAnimation(0f, 0f, 200f, 0f)
            .apply {
                duration = animDuration
                interpolator = animInterpolator
            }.run { binding.cvLocation.startAnimation(this) }
    }
}