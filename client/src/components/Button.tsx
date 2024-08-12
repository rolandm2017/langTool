import React from "react"

type ButtonVariant = "default" | "outline" | "ghost"

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: ButtonVariant
    children: React.ReactNode
}

const Button: React.FC<ButtonProps> = ({
    children,
    onClick,
    variant = "default",
    className = "",
    ...props
}) => {
    const baseStyles =
        "px-4 py-2 rounded-md font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2"

    const variantStyles: Record<ButtonVariant, string> = {
        default: "bg-blue-500 text-white hover:bg-blue-600 focus:ring-blue-500",
        outline:
            "bg-transparent text-blue-500 border border-blue-500 hover:bg-blue-50 focus:ring-blue-500",
        ghost: "bg-transparent text-blue-500 hover:bg-blue-50 focus:ring-blue-500",
    }

    const buttonStyles = `${baseStyles} ${variantStyles[variant]} ${className}`

    return (
        <button className={buttonStyles} onClick={onClick} {...props}>
            {children}
        </button>
    )
}

export default Button
